package org.example.fourtreesproject.user.model.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class UserInfoResponse {
    private String name;
    private String email;
    private LocalDate birth;
    private String sex;
    private String address;
    private Integer postCode;
    private String phoneNumber;
}
