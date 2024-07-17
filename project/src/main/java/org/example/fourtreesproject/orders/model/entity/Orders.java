package org.example.fourtreesproject.orders.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.user.model.entity.User;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, length = 50)
    private String recipientName;

    @Column(nullable = false, length = 50)
    private String recipientPhoneNumber;

    @Column(nullable = false, length = 100)
    private String recipientAddress;

    @Column(nullable = false)
    private Integer recipientPostCode;

    @Column(nullable = false)
    @Min(value = 1, message = "수량은 최소 1개 이상이여야 합니다.")
    private Integer orderQuantity;

    @Column(nullable = false, length = 10)
//    @ColumnDefault(value = "주문")
    private String orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderStartedAt;
    private LocalDateTime orderCancledAt;
    private LocalDateTime orderCompletedAt;
    private String deliveryNumber;

    @ManyToOne
    @JoinColumn(name = "gpbuy_idx")
    private GroupBuy groupBuy;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private User user;
}
