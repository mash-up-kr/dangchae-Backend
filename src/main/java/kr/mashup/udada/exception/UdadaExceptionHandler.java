package kr.mashup.udada.exception;

import io.jsonwebtoken.ExpiredJwtException;
import kr.mashup.udada.user.dto.common.ResponseDTO;
import kr.mashup.udada.user.exception.EmptyTokenException;
import kr.mashup.udada.user.exception.InvalidTokenException;
import kr.mashup.udada.user.exception.NeedSignUpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UdadaExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity BadRequestException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity UnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity BaseException(BaseException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity ResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity invalidAppleCodeException() {
        return ResponseEntity.badRequest().body(ResponseDTO.builder().data("invalid vendor token").build());
    }

    @ExceptionHandler(NeedSignUpException.class)
    public ResponseEntity needSignUpException() {
        return ResponseEntity.status(404).body(ResponseDTO.builder().data("Need Sign Up").build());
    }

    @ExceptionHandler(EmptyTokenException.class)
    public ResponseEntity emptyTokenException() {
        return ResponseEntity.status(400).body(ResponseDTO.builder().data("Need Token").build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity expiredJwtException() {
        return ResponseEntity.status(400).body(ResponseDTO.builder().data("Need To Refresh Token").build());
    }
}
