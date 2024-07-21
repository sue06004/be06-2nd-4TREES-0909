package org.example.fourtreesproject.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.exception.custom.InvalidCustomException;
import org.example.fourtreesproject.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.controller.UserController;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(value = 1)
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<String> handleGlobalException(InvalidCustomException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
