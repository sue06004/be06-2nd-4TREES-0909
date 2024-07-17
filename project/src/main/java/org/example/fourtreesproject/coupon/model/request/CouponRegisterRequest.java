package org.example.fourtreesproject.coupon.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponRegisterRequest {
    private String couponName;
    private Integer couponPrice;
    private String couponContent;
    private Integer minOrderPrice;
}
