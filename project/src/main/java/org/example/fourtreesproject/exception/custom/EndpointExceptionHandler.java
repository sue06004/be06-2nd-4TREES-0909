package org.example.fourtreesproject.exception.custom;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.common.BaseResponseStatus;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class EndpointExceptionHandler {

    @ExceptionHandler({Exception.class})
    public BaseResponse<String> handleGlobalException(Exception e) {
        return new BaseResponse<>(BaseResponseStatus.REQUEST_FAIL);
    }
}
