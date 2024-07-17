package org.example.fourtreesproject.user.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserSignupRequest {
    private String name;
    private String password;
    private LocalDate birth;
    private String email;
    private String sex;
    private String address;
    private int postCode;
    private String phoneNumber;
}
