package com.aburakkontas.manga_payment.api;

import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<String>> handleException(Exception ex) {
        if (ex.getCause() instanceof ExceptionWithErrorCode) {
            return new ResponseEntity<>(Result.failure(ex.getCause().getMessage(), ((ExceptionWithErrorCode) ex.getCause()).getCode()), HttpStatusCode.valueOf(((ExceptionWithErrorCode) ex.getCause()).getCode()));
        } else {
            return new ResponseEntity<>(Result.failure(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
