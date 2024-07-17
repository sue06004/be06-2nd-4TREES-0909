package org.example.fourtreesproject.user.exception;

import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidPasswordException(BaseResponseStatus status) {
        this.status = status;
    }
}
