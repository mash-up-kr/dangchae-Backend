package kr.mashup.udada.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        this(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public UnauthorizedException(String msg) {
        super(ErrorModel.builder()
                .msg(msg)
                .build());
    }
}
