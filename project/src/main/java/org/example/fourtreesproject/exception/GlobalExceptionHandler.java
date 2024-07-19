package org.example.fourtreesproject.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.exception.custom.InvalidUserException;
import org.example.fourtreesproject.user.controller.UserController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<String> handleGlobalException(InvalidUserException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
