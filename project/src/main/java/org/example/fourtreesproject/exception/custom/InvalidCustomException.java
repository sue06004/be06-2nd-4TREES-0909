package org.example.fourtreesproject.exception.custom;

import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidCustomException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidCustomException(BaseResponseStatus status) {
        this.status = status;
    }
}
