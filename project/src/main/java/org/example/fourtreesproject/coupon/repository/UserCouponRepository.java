package org.example.fourtreesproject.coupon.repository;

import org.example.fourtreesproject.coupon.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    Optional<UserCoupon> findFirstByIdxAndUserIdxAndCouponStatusTrueOrderByIdx(Long idx, Long userIdx);
}
