package org.example.fourtreesproject.groupbuy.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.example.fourtreesproject.groupbuy.exception.customs.GroupBuyRegisterFailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GroupBuyExceptionHandler {


//    @ExceptionHandler(value = Exception.class)
//    public BaseResponse fail(){
//        return new BaseResponse(BaseResponseStatus.);
//    }

    @ExceptionHandler(value = GroupBuyRegisterFailException.class)
    public BaseResponse registerFail(){
        return new BaseResponse(BaseResponseStatus.GROUPBUY_RREGIST_FAIL);
    }


}
