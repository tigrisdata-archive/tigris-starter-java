package com.tigrisdata.example.controller;

import com.tigrisdata.db.client.error.TigrisDBException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TigrisDBExceptionAdvice {

  @ResponseBody
  @ExceptionHandler(TigrisDBException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String employeeNotFoundHandler(TigrisDBException ex) {
    return ex.getMessage();
  }
}
