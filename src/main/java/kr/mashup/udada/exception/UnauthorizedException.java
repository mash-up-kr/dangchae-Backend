package kr.mashup.udada.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class UnauthorizedException extends BaseException{
    public UnauthorizedException() {
        this(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public UnauthorizedException(String msg) {
        this(HttpStatus.UNAUTHORIZED.value(), msg);
    }

    public UnauthorizedException(int code, String msg) {
        super(ErrorModel.builder()
                .code(code)
                .msg(msg)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
