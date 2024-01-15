package kr.co.writenow.writenow.exception.user;

import kr.co.writenow.writenow.exception.CustomException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
