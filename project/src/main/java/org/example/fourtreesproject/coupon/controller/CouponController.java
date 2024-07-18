package org.example.fourtreesproject.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.coupon.model.request.CouponRegisterRequest;
import org.example.fourtreesproject.coupon.service.CouponService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "쿠폰 등록 api")
    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody CouponRegisterRequest couponRegisterRequest){
        couponService.couponRegister(couponRegisterRequest);
        return new BaseResponse<>();
    }
}
