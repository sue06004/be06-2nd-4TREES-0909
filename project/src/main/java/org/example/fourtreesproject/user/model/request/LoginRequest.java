package org.example.fourtreesproject.user.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//스웨거 로그인용 dto
public class LoginRequest {
    private String email;
    private String password;
}
