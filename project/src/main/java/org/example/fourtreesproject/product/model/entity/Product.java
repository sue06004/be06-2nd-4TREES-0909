package org.example.fourtreesproject.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.company.model.entity.Company;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="product")
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
    private LocalDate productRegedAt; //상품등록일
    private LocalDateTime productModifiedAt; //상품수정일
    @Column(nullable = false, length = 50)
    private String productStatus;
    private LocalDateTime productDelAt; //상품삭제일

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "product")
    private ProductImg productImg;

}
