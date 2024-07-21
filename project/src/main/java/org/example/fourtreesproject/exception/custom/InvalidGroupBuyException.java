package org.example.fourtreesproject.exception.custom;

import org.example.fourtreesproject.common.BaseResponseStatus;

public class InvalidGroupBuyException extends InvalidCustomException{

    public InvalidGroupBuyException(BaseResponseStatus status) {
        super(status);
    }
}
