package org.example.fourtreesproject.user.repository;

import org.example.fourtreesproject.user.model.entity.SellerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerDetailRepository extends JpaRepository<SellerDetail, Long> {
    Optional<SellerDetail> findByUserIdx(Long sellerIdx);

}
