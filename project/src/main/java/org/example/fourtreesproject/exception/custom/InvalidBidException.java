package org.example.fourtreesproject.exception.custom;


import org.example.fourtreesproject.common.BaseResponseStatus;


public class InvalidBidException extends InvalidCustomException {

    public InvalidBidException(BaseResponseStatus status) {
        super(status);
    }
}
