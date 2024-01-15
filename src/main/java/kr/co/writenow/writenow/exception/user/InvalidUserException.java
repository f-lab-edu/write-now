package kr.co.writenow.writenow.exception.user;

import kr.co.writenow.writenow.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidUserException extends CustomException {

    public InvalidUserException(HttpStatus status, String message) {
        super(status, message);
    }
}
