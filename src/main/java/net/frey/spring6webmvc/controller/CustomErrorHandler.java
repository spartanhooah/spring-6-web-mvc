package net.frey.spring6webmvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<List<Map<String, String>>> handleBindErrors(
            MethodArgumentNotValidException exception) {
        List<Map<String, String>> errorList =
                exception.getFieldErrors().stream()
                        .map(
                                error -> {
                                    Map<String, String> errorMap = new HashMap<>();
                                    errorMap.put(error.getField(), error.getDefaultMessage());
                                    return errorMap;
                                })
                        .toList();

        return ResponseEntity.badRequest().body(errorList);
    }
}
