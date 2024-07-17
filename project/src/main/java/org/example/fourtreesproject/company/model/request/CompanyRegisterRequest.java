package org.example.fourtreesproject.company.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Builder
@Getter
@Setter
public class CompanyRegisterRequest {
    @NotNull
    private String companyName;
    @NotNull
    private String companyAddress;
    @NotNull
    private Integer companyPostCode;
    @NotNull
    private String companyType;
    @NotNull
    private String companyIntro;

}
