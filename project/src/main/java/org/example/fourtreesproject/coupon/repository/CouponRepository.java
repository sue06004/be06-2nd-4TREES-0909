package org.example.fourtreesproject.coupon.repository;

import org.example.fourtreesproject.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
