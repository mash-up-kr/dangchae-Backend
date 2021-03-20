package kr.mashup.udada.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException() {
        this(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public ResourceNotFoundException(String msg) {
        super(ErrorModel.builder()
                .msg(msg)
                .build());
    }
}