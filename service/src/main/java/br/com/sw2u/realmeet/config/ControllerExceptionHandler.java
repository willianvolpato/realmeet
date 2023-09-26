package br.com.sw2u.realmeet.config;

import br.com.sw2u.realmeet.api.model.ResponseError;
import br.com.sw2u.realmeet.exception.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(Exception e) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, e);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, Exception e) {
        return new ResponseEntity<>(
            new ResponseError().code(status.value()).status(status.getReasonPhrase()).message(e.getMessage()),
            status
        );
    }
}
