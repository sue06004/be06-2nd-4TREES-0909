package org.example.fourtreesproject.groupbuy.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.product.model.entity.Product;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<GroupBuy> groupbuyList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private List<Product> productList;
}
