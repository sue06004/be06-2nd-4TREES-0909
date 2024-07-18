package org.example.fourtreesproject.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.bid.model.entity.Bid;
import org.example.fourtreesproject.company.model.entity.Company;
import org.example.fourtreesproject.groupbuy.model.entity.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 100)
    private String productName;
    @Column(columnDefinition = "TEXT", nullable = true)
    private String productContent;

    @Builder.Default
    private LocalDateTime productRegedAt = LocalDateTime.now(); //상품등록일

    private LocalDateTime productModifiedAt; //상품수정일

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String productStatus = "등록";

    private LocalDateTime productDelAt; //상품삭제일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_idx")
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImg> productImgList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Bid> bidList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;

}
