package org.example.fourtreesproject.user.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.user.controller.UserController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.example.fourtreesproject.common.BaseResponseStatus.USER_REGISTER_FAIL_PASSWORD_RULE;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionHandler {

    @ExceptionHandler(InvalidPasswordException.class)
    public BaseResponse<String> handleInvalidPasswordException(InvalidPasswordException e) {
        return new BaseResponse<>(USER_REGISTER_FAIL_PASSWORD_RULE);
    }
}
