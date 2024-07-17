package org.example.fourtreesproject.company.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourtreesproject.product.model.entity.Product;
import org.example.fourtreesproject.user.model.entity.User;

import java.util.List;

@Entity
@Table(name="company")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(nullable = false, length = 100)
    private String companyName;
    @Column(nullable = false, length = 255)
    private String companyAddress;
    @Column(nullable = false)
    private Integer companyPostCode;
    @Column(nullable = false, length = 20)
    private String companyType;
    @Column(columnDefinition = "TEXT", nullable = true)
    private String companyInfo;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Product> product;

}
