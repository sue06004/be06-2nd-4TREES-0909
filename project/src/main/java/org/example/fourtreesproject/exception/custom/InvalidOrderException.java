package org.example.fourtreesproject.exception.custom;

import org.example.fourtreesproject.common.BaseResponseStatus;

public class InvalidOrderException extends InvalidCustomException {

    public InvalidOrderException(BaseResponseStatus status) {
        super(status);
    }
}
