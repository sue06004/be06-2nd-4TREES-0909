package org.example.fourtreesproject.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupResponse {
    private String name;
    private String password;
    private LocalDateTime birth;
    private String email;
    private String sex;
    private String address;
    private int postCode;
    private String phoneNumber;
}
