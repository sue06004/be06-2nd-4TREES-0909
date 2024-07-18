package org.example.fourtreesproject.companyRegVerify.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CompanyRegVerifyRequest {
    private String b_no;
    private String p_nm;
    private String start_dt;
}
