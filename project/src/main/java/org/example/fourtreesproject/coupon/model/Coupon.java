package org.example.fourtreesproject.coupon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Table(name="coupon")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 100)
    @JoinColumn(name = "coupon_name")
    private String couponName;
    @Column(nullable = false)
    @JoinColumn(name = "coupon_price")
    private Integer couponPrice;
    @Column(nullable = false, columnDefinition = "TEXT")
    @JoinColumn(name = "coupon_content")
    private String couponContent;
    @Column(nullable = false)
    @JoinColumn(name = "coupon_price")
    private Integer minOrderPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon")
    private List<UserCoupon> userCouponList;
}
