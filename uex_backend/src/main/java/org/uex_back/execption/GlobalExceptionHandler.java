package org.uex_back.execption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.uex_back.dto.error.ErrorMessageResponse;
import org.uex_back.exceptionhandler.CpfAlreadyInUseException;
import org.uex_back.exceptionhandler.EmailAlreadyInUseException;
import org.uex_back.exceptionhandler.InvalidCpfException;
import org.uex_back.exceptionhandler.InvalidPasswordException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorMessageResponse> handleEmailAlreadyInUse(EmailAlreadyInUseException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorMessageResponse> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(CpfAlreadyInUseException.class)
    public ResponseEntity<ErrorMessageResponse> handleCpfAlreadyInUse(CpfAlreadyInUseException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCpfException.class)
    public ResponseEntity<ErrorMessageResponse> handleInvalidCpf(InvalidCpfException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(ex.getMessage()));
    }


}