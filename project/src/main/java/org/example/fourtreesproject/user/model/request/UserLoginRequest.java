package org.example.fourtreesproject.user.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}
