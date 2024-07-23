package org.example.fourtreesproject.company.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CompanyDetailResponse {
    private String companyName;
    private String companyAddress;
    private Integer companyPostCode;
    private String companyType;
    private String companyIntro;
}
