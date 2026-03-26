package com.zosh.service.serviceImpl;
import com.zosh.exceptions.ProductException;
import com.zosh.model.Category;
import com.zosh.model.Product;
import com.zosh.model.Seller;
import com.zosh.repository.CategoryRepository;
import com.zosh.repository.ProductRepository;
import com.zosh.request.CreateProductRequest;
import com.zosh.service.ProductService;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate ;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest req, Seller seller) {

        Category category1 = categoryRepository.findByCategoryId(req.getCategory());
        if(category1 == null) {
            category1 = new Category();
            category1.setCategoryId(req.getCategory());
            category1.setName(req.getCategory());
            category1.setLevel(1);
            category1 = categoryRepository.save(category1);
        }
        Category category2 = categoryRepository.findByCategoryId(req.getCategory2());
        if(category2 == null){
            category2 = new Category();
            category2.setCategoryId(req.getCategory2());
            category2.setName(req.getCategory2());
            category2.setLevel(2);
            category2.setParentCategory(category1);
            category2 = categoryRepository.save(category2);
        } else {
            category2.setParentCategory(category1);
            categoryRepository.save(category2);
        }
        Category category3 = categoryRepository.findByCategoryId(req.getCategory3());
        if(category3 == null){
            category3 = new Category();
            category3.setCategoryId(req.getCategory3());
            category3.setName(req.getCategory3());
            category3.setLevel(3);
            category3.setParentCategory(category2);
            category3 = categoryRepository.save(category3);
        } else {
            category3.setParentCategory(category2);
            categoryRepository.save(category3);
        }

            int discountPercentage= calculateDiscountpercentage( req.getMrpPrice(),req.getSellingPrice());

            Product product = new Product();
            product.setSeller(seller);
            product.setCategory(category3);
            product.setDescription(req.getDescription());
            product.setCreateAt(LocalDateTime.now());
            product.setTitle(req.getTitle());
            product.setColor(req.getColor());
            product.setQuantity(req.getQuantity());
            product.setBrand(req.getBrand());
            product.setImages(req.getImages());
            product.setMrpPrice(req.getMrpPrice());
            product.setSellingPrice(req.getSellingPrice());
            product.setSizes(req.getSizes());
            product.setDiscountPercentage(discountPercentage);
            return productRepository.save(product);
    }

    private int calculateDiscountpercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0){
            throw new IllegalArgumentException("Actual price must be greater than 0");
        }
        int  discountPercentage= (int) Math.round(( (mrpPrice - sellingPrice) * 100.0 ) / mrpPrice);
        return discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
    findProductById(productId);
    product.setId(productId);
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return productRepository.findById(productId)
                .orElseThrow(()-> new ProductException("product not found with id " +productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProduct(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String color, String sizes, Integer minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort, String stock, Integer pageNumber) {
        Specification<Product> spec =(root, query, criteriaBuilder)-> {
            List<Predicate> predicates=new ArrayList<>();

            if(category!=null && !category.isEmpty()) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add( criteriaBuilder.equal(categoryJoin.get("categoryId"), category));
            }
            if(color!=null && !color.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("color"),color));
            }
            if(sizes != null && !sizes.isEmpty()){
                predicates.add(criteriaBuilder.equal(root.get("sizes"), sizes));
            }

            if(stock != null){
                if("in_stock".equalsIgnoreCase(stock)){
                    predicates.add(criteriaBuilder.greaterThan(root.get("quantity"), 0));
                } else if("out_of_stock".equalsIgnoreCase(stock)){
                    predicates.add(criteriaBuilder.equal(root.get("quantity"), 0));
                }
            }
            if(brand!=null && !brand.isEmpty()){
                predicates.add( criteriaBuilder.equal(root.get("brand"),brand));
            }
            if(minPrice!=null ){
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"),minPrice));
            }
            if(maxPrice!=null ){
                predicates.add( criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"),maxPrice));
            }
            if(minDiscount!=null ){
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"),minDiscount));
            }

           return criteriaBuilder.and((predicates.toArray(new Predicate[0])));
            };
             Pageable pageable;
                    if(sort!=null && !sort.isEmpty()){
                    switch (sort){
                case "price_low":
                    pageable=  PageRequest.of(pageNumber!=null?pageNumber:0,10, Sort.by("sellingPrice").ascending());
                    break;
                case "price_high":
                    pageable=  PageRequest.of(pageNumber!=null?pageNumber:0,10, Sort.by("sellingPrice").descending());
                    break;
                default:
                    pageable=  PageRequest.of(pageNumber!=null?pageNumber:0,10, Sort.unsorted());
                    };
                }else{
            pageable=  PageRequest.of(pageNumber!=null ? pageNumber:0,10,Sort.unsorted());
                 }
            return productRepository.findAll(spec,pageable);

            }
    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
