package org.example.fourtreesproject.orders.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.fourtreesproject.common.BaseResponse;
import org.example.fourtreesproject.orders.controller.OrdersController;
import org.example.fourtreesproject.orders.exception.custom.InvalidOrderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = {OrdersController.class})
public class OrderExceptionHandler {

    @ExceptionHandler(InvalidOrderException.class)
    public BaseResponse<String> handleInvalidOrderException(InvalidOrderException e) {
        log.debug(e.getMessage());
        return new BaseResponse<>(e.getStatus());
    }
}
