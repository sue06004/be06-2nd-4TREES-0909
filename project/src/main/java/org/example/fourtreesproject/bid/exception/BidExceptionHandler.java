package org.example.fourtreesproject.bid.exception;

import org.example.fourtreesproject.bid.exception.customs.InvalidBidException;
import org.example.fourtreesproject.common.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class BidExceptionHandler {
    @ExceptionHandler(InvalidBidException.class)
    public BaseResponse<String> handleBidServiceException(InvalidBidException e) {
        return new BaseResponse<>(e.getStatus());
    }
}
