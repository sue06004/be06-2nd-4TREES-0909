package org.example.fourtreesproject.exception.custom;

import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidUserException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidUserException(BaseResponseStatus status) {
        this.status = status;
    }
}
