package kr.mashup.udada.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorModel {

    private int code;
    private String msg;
    private LocalDateTime timestamp;
}
