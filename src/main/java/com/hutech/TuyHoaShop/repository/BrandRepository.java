package com.hutech.TuyHoaShop.repository;
import com.hutech.TuyHoaShop.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
