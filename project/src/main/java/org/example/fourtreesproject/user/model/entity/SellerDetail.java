package org.example.fourtreesproject.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Table(name = "seller_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SellerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 50)
    private String sellerBank;
    @Column(nullable = false, length = 50)
    private String sellerDepoName;    // 예금주명
    @Column(nullable = false, length = 50)
    private String sellerAccount;
    @Column(nullable = false, length = 20)
    private String sellerRegNum;     // 사업자등록번호
    @Column(nullable = false, length = 20)
    private String sellerMosNum;     // 통신판매업신고번호
    private LocalDate sellerOpenedAt;  // 개업일자

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}
