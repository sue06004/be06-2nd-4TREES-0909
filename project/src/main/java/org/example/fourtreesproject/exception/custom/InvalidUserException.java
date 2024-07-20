package org.example.fourtreesproject.exception.custom;

import org.example.fourtreesproject.common.BaseResponseStatus;

public class InvalidUserException extends RuntimeException {

    public InvalidUserException(BaseResponseStatus status) {
        super(status.getMessage());
    }
}
