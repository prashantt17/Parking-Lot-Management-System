package com.example.parking.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalState(IllegalStateException ex, WebRequest req) {
    return ResponseEntity.status(409).body(ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex, WebRequest req) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleAll(Exception ex, WebRequest req) {
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }
}
