package kr.mashup.udada.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UdadaExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorModel> handleException(BaseException e) {
        ErrorModel error = e.error;
        log.error("REST API error : {} ", error.getMsg());

        switch (error.getCode()) {
            case 400:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            case 401:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            default:
                throw new RuntimeException();
        }
    }
}
