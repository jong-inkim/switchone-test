package com.switchwon.handler;

import com.switchwon.payment.exception.DoNotMachedMerchantIdException;
import com.switchwon.payment.exception.DoNotMatchedAmountException;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.exception.NotEnoughBalanceException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DoNotMachedMerchantIdException.class)
    public ResponseEntity<ErrorResponse> handleDoNotMachedMerchantIdException(DoNotMachedMerchantIdException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e.getMessage(), 403));
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughBalanceException(NotEnoughBalanceException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e.getMessage(), 400));
    }

    @ExceptionHandler(DupliatedMerchantIdException.class)
    public ResponseEntity<ErrorResponse> handleDupliatedMerchantIdException(DupliatedMerchantIdException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e.getMessage(), 400));
    }

    @ExceptionHandler(DoNotMatchedAmountException.class)
    public ResponseEntity<ErrorResponse> handleDoNotMatchedAmountException(DoNotMatchedAmountException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e.getMessage(), 403));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e.getMessage(), 404));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500)
                .body(new ErrorResponse(e.getMessage(), 500));
    }
}
