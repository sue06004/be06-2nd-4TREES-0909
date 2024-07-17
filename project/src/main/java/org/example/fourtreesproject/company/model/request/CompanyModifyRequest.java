package org.example.fourtreesproject.company.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter
@Setter
public class CompanyModifyRequest {
    private String companyName;
    private String companyAddress;
    private Integer companyPostCode;
    private String companyType;
    private String companyIntro;

}
