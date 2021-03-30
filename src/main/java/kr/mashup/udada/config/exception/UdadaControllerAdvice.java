package kr.mashup.udada.config.exception;

import kr.mashup.udada.user.dto.common.ResponseDTO;
import kr.mashup.udada.user.exception.InvalidTokenException;
import kr.mashup.udada.user.exception.NeedSignUpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UdadaControllerAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity invalidAppleCodeException() {
        return ResponseEntity.badRequest().body(ResponseDTO.builder().data("invalid vendor token").build());
    }

    @ExceptionHandler(NeedSignUpException.class)
    public ResponseEntity needSignUpException() {
        return ResponseEntity.status(404).body(ResponseDTO.builder().data("Need Sign Up").build());
    }
}
