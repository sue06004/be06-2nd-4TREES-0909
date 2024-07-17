package org.example.fourtreesproject.orders.exception;

import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.orders.controller.OrdersController;
import org.example.fourtreesproject.orders.exception.custom.InvalidOrderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {OrdersController.class})
public class OrderExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public BaseResponse<String> handleInvalidOrderException(InvalidOrderException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
