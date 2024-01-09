package kr.co.writenow.writenow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message){
        super(message);
        this.status = status;
        this.message = message;
    }
}
