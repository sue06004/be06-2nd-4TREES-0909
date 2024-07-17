package org.example.fourtreesproject.orders.exception.custom;

import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidOrderException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidOrderException(BaseResponseStatus status) {
        this.status = status;
    }
}
