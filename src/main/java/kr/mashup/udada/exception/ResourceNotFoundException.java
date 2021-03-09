package kr.mashup.udada.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException() {
        this(HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public ResourceNotFoundException(String msg) {
        this(HttpStatus.NOT_FOUND.value(), msg);
    }

    public ResourceNotFoundException(int code, String msg) {
        super(ErrorModel.builder()
                .code(code)
                .msg(msg)
                .timestamp(LocalDateTime.now())
                .build());
    }
}