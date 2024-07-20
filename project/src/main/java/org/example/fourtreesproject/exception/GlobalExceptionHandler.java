package org.example.fourtreesproject.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.exception.custom.InvalidCustomException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCustomException.class)
    public BaseResponse<String> handleGlobalException(InvalidCustomException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
