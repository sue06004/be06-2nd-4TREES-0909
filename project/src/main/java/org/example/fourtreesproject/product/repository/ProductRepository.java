package org.example.fourtreesproject.product.repository;

import org.example.fourtreesproject.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCompanyIdx(Long companyIdx);
}
