package org.example.fourtreesproject.companyRegVerify.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name="company_reg_verify")
@AllArgsConstructor
@Getter
@Builder
public class CompanyRegVerify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String companyReg;
    @Column(nullable = false)
    private String uuid;
}
