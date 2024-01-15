package kr.co.writenow.writenow.exception.handler;

import kr.co.writenow.writenow.exception.CustomException;
import kr.co.writenow.writenow.exception.ExceptionResponse;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    public static Map<String, String> validateErrorsHandler(Errors errors){
        Map<String, String> map = new HashMap<>();

        for (FieldError fieldError : errors.getFieldErrors()) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return map;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e){
        return ResponseEntity
                .status(e.getStatus())
                .body(new ExceptionResponse(e.getStatus().value(), e.getMessage()));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> invalidParameterExceptionHandler(InvalidParameterException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }
}
