package kr.mashup.udada.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException() {
        this(HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    public BadRequestException(String msg) {
        super(ErrorModel.builder()
                .msg(msg)
                .build());
    }
}
