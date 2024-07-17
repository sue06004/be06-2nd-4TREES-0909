package org.example.fourtreesproject.user.model.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import java.time.LocalDate;

@Setter
@Getter
public class SellerSignupRequest {
    private String name;
    private String password;
    private LocalDate birth;
    private String email;
    private String sex;
    private String address;
    private int postCode;
    private String phoneNumber;
    private String sellerBank;
    private String sellerDepoName;
    private String sellerAccount;
    private String sellerRegNum;     // 사업자등록번호
    private String sellerMosNum;     // 통신판매업신고번호
    private LocalDate sellerOpenedAt;  // 개업일자

}
