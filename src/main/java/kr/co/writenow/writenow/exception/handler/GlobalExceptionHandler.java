package kr.co.writenow.writenow.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import kr.co.writenow.writenow.exception.CustomException;
import kr.co.writenow.writenow.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
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
import java.util.Set;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    public <T> void errorLogging(Class<T> clazz, String message){
        log.error(String.format("Error Class: %s, message: %s", clazz.getName(), message));
    }

    public static Map<String, String> validateErrorsHandler(Errors errors){
        Map<String, String> map = new HashMap<>();

        for (FieldError fieldError : errors.getFieldErrors()) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return map;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e){
        errorLogging(e.getClass(), e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ExceptionResponse(e.getStatus().value(), e.getMessage()));
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> invalidParameterExceptionHandler(InvalidParameterException e){
        errorLogging(e.getClass(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        errorLogging(e.getClass(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e){
        errorLogging(e.getClass(), e.getMessage());
        Map<String, String> errors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        constraintViolations.forEach(cv-> {
            Path path = cv.getPropertyPath();
            if(path instanceof PathImpl pathImpl) {
                NodeImpl leafNode = pathImpl.getLeafNode();
                errors.put(leafNode.getName(), cv.getMessage());
            }
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }
}
