package org.example.fourtreesproject.exception.custom;


import org.example.fourtreesproject.common.BaseResponseStatus;

public class InvalidCompanyException extends InvalidCustomException {

    public InvalidCompanyException(BaseResponseStatus status) {
        super(status);
    }
}
