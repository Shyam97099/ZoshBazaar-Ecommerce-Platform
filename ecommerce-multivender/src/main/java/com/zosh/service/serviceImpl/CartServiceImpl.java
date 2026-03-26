package com.zosh.service.serviceImpl;

import com.zosh.model.Cart;
import com.zosh.model.CartItem;
import com.zosh.model.Product;
import com.zosh.model.User;
import com.zosh.repository.CartItemRepository;
import com.zosh.repository.CartRepository;
import com.zosh.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
    Cart cart= findUserCart(user);
    CartItem isPresent=cartItemRepository.findByCartAndProductAndSize(cart,product,size);
    if(isPresent==null){
        CartItem cartItem=new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setUserId(user.getId());
        cartItem.setSize(size);
        cartItem.setMrpPrice(quantity * product.getMrpPrice());
        int totalPrice=quantity*product.getSellingPrice();
        cartItem.setSellingPrice(totalPrice);
        cart.getCartItems().add(cartItem);
        cartItem.setCart(cart);
        return cartItemRepository.save(cartItem);
    }
        return isPresent;
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart=cartRepository.findByUserId(user.getId());
        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartItem:cart.getCartItems()){
            totalPrice+=cartItem.getMrpPrice();
            totalDiscountedPrice+=cartItem.getSellingPrice();
            totalItem+=cartItem.getQuantity();
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountpercentage(totalPrice,totalDiscountedPrice));

        return cart;
    }
    private int calculateDiscountpercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0){
//            throw new IllegalArgumentException("Actual price must be greater than 0");
            return 0;
        }
        int  discountPercentage= (int) Math.round(( (mrpPrice - sellingPrice) * 100.0 ) / mrpPrice);
        return discountPercentage;
    }
}
