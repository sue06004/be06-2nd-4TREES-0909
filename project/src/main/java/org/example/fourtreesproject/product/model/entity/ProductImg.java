package org.example.fourtreesproject.product.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="product_img")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 255)
    private String productImgUrl;
    @Column(nullable = false)
    private Integer productImgSequence;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;
}
