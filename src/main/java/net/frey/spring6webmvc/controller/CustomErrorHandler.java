package net.frey.spring6webmvc.controller;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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

    @ExceptionHandler(TransactionSystemException.class)
    ResponseEntity<?> handleJpaViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ve =
                    (ConstraintViolationException) exception.getCause().getCause();

            List<Map<String, String>> errorList =
                    ve.getConstraintViolations().stream()
                            .map(
                                    violation -> {
                                        Map<String, String> errorMap = new HashMap<>();
                                        errorMap.put(
                                                violation.getPropertyPath().toString(),
                                                violation.getMessage());

                                        return errorMap;
                                    })
                            .toList();

            return responseEntity.body(errorList);
        }

        return responseEntity.build();
    }
}
