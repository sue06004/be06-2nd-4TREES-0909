package org.example.fourtreesproject.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.coupon.model.Coupon;
import org.example.fourtreesproject.coupon.model.request.CouponRegisterRequest;
import org.example.fourtreesproject.coupon.model.response.CouponRegisterResponse;
import org.example.fourtreesproject.coupon.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponRegisterResponse couponRegister(CouponRegisterRequest couponRegisterRequest){
        Coupon coupon = Coupon.builder()
                .couponContent(couponRegisterRequest.getCouponContent())
                .couponName(couponRegisterRequest.getCouponName())
                .couponPrice(couponRegisterRequest.getCouponPrice())
                .minOrderPrice(couponRegisterRequest.getMinOrderPrice())
                .build();
        couponRepository.save(coupon);
        return CouponRegisterResponse.builder()
                .couponIdx(coupon.getIdx())
                .build();
    }
}
