package com.zosh.repository;
import com.zosh.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {
    List<Product> findBySellerId(Long id);
    @Query("""
       SELECT p FROM Product p
       LEFT JOIN p.category c
       WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))
          OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
       """)
    List<Product> searchProduct(@Param("query") String query);

}
