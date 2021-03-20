package kr.mashup.udada.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorModel {
    private String msg;
}
