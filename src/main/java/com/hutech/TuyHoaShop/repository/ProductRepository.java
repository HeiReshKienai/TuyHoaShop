package com.hutech.TuyHoaShop.repository;
import com.hutech.TuyHoaShop.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p JOIN OrderDetail od ON p.name = od.productName GROUP BY p.id ORDER BY SUM(od.quantity) DESC")
    List<Product> findTopProducts(Pageable pageable);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByBrandId(Long brandId);

    List<Product> findByNameContaining(String name);
}
