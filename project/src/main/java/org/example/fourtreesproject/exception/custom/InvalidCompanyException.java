package org.example.fourtreesproject.exception.custom;


import lombok.Getter;
import org.example.fourtreesproject.common.BaseResponseStatus;

@Getter
public class InvalidCompanyException extends RuntimeException {
    private final BaseResponseStatus status;

    public InvalidCompanyException(BaseResponseStatus status) {
        this.status = status;
    }
}
