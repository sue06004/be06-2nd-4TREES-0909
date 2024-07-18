package org.example.fourtreesproject.user.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.user.controller.UserController;
import org.example.fourtreesproject.user.exception.custom.InvalidUserException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionHandler {

    @ExceptionHandler(InvalidUserException.class)
    public BaseResponse<String> handleInvalidUserException(InvalidUserException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
