package org.example.fourtreesproject.product.repository;

import org.example.fourtreesproject.product.model.entity.ProductImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long>{
    Optional<ProductImg> findByProductIdxAndProductImgSequence(Long productId, Integer seq);
}
