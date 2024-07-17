package org.example.fourtreesproject.user.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SellerInfoResponse {
    private String name;
    private LocalDate birth;
    private String sex;
    private String email;
    private String address;
    private Integer postCode;
    private String phoneNumber;
    private String sellerBank;
    private String sellerDepoName;
    private String sellerAccount;
    private LocalDate sellerOpenedAt;
    private String sellerRegNum;
    private String sellerMosNum;
}
