package org.example.fourtreesproject.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.company.model.entity.Company;
import org.example.fourtreesproject.delivery.model.DeliveryAddress;
import org.example.fourtreesproject.groupbuy.model.entity.GroupBuy;
import org.example.fourtreesproject.orders.model.entity.Orders;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 50)
    private String type;   // inapp, kakao, naver ...
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 100)
    private String password;
    @Builder.Default
    private String role = "ROLE_USER";
    @Column(nullable = false, length = 100)
    private String email;
    @Column(length = 50)
    private String phoneNumber;
    private LocalDate birth;
    @Column(length = 10)
    private String sex;
    @Column(length = 10)
    private String status;
    @Builder.Default
    private LocalDateTime regedAt = LocalDateTime.now();
    private LocalDateTime quitedAt;
    private String address;
    private int postCode;
    @Column(nullable = false)
    @Builder.Default
    private boolean emailStatus = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<DeliveryAddress> deliveryAddress;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserDetail userDetails;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private SellerDetail sellerDetail;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<GroupBuy> groupBuy;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Company company;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user")
    private List<Orders> ordersList;

    public void updateEmailStatus() {
        this.emailStatus = true;
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}
