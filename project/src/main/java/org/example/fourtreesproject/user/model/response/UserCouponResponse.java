package org.example.fourtreesproject.user.model.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserCouponResponse {

    private Long userCouponIdx;
    private String couponName;
    private String couponContent;
    private Integer couponPrice;
    private Integer minOrderPrice;
}
