package org.example.fourtreesproject.product.repository;

import org.example.fourtreesproject.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "JOIN FETCH p.company c " +
            "JOIN FETCH p.productImgList pil " +
            "JOIN FETCH p.category " +
            "WHERE p.company.idx = :companyIdx")
    List<Product> findByCompanyIdx(Long companyIdx);
}
