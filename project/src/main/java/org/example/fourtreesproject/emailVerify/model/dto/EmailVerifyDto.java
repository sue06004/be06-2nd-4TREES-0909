package org.example.fourtreesproject.emailVerify.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerifyDto {
    private String email;
    private String uuid;
}
