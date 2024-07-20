package org.example.fourtreesproject.exception.custom;

import org.example.fourtreesproject.common.BaseResponseStatus;

public class GroupBuyRegisterFailException extends InvalidCustomException{

    public GroupBuyRegisterFailException(BaseResponseStatus status) {
        super(status);
    }
}
