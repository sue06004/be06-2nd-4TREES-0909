package org.example.fourtreesproject.bid.exception.customs;


import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidBidException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidBidException(BaseResponseStatus status) {
        this.status = status;
    }
}
