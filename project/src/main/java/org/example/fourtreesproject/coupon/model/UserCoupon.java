package org.example.fourtreesproject.coupon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.orders.model.entity.Orders;
import org.example.fourtreesproject.user.model.entity.User;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private LocalDateTime couponIssuedAt;
    private LocalDateTime couponExpiredAt;
    private Boolean couponStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    private Coupon coupon;

    public void useCoupon(){
        this.couponStatus = false;
    }
    public void cancleCoupon(){
        this.couponStatus = true;
    }
}
