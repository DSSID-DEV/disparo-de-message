package com.dssiddev.disparowhatsapp.handles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(getAllErros(ex));
    }

    private List<String> getAllErros(MethodArgumentNotValidException e) {
        List<String> messageErros = new ArrayList<>();
        for(ObjectError objectError:  e.getAllErrors()) {
            messageErros.add(objectError.getDefaultMessage());
        }
        return messageErros;
    }
}
