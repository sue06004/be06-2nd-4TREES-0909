package org.example.fourtreesproject.coupon.model.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.coupon.model.UserCoupon;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private String couponName;
    private Integer couponPrice;
    private String couponContent;
    private Integer minOrderPrice;
}
