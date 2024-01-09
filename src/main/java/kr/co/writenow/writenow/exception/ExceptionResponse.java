package kr.co.writenow.writenow.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExceptionResponse {
    private int code;
    private String message;

}
