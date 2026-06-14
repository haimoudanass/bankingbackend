package com.yassirrijali.bankingbackend.web;

import com.yassirrijali.bankingbackend.exceptions.BalanceNotSufficientException;
import com.yassirrijali.bankingbackend.exceptions.BankAccountNotFoundException;
import com.yassirrijali.bankingbackend.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankAccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(BankAccountNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BalanceNotSufficientException.class)
    public ResponseEntity<String> handleBalanceNotSufficient(BalanceNotSufficientException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleCustomerNotFound(CustomerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}