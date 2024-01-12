package kr.co.writenow.writenow.exception.user;

import kr.co.writenow.writenow.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserRegisterException extends CustomException {


    public UserRegisterException(HttpStatus status, String message) {
        super(status, message);
    }
}
