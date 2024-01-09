package kr.co.writenow.writenow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    int errorCode();
    String message();
}
