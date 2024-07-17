package org.example.fourtreesproject.bid.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private Integer bidPrice;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String bidStatus = "등록";

    @Column(nullable = false)
    @Builder.Default
    private Boolean bidSelect = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime bidRegedAt = LocalDateTime.now();

    private LocalDateTime bidDeletedAt;

    private LocalDateTime bidModifiedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_idx")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="gpbuy_idx")
    private GroupBuy groupBuy;
}
