package kr.co.writenow.writenow.controller.user;

import jakarta.validation.Valid;
import kr.co.writenow.writenow.exception.handler.GlobalExceptionHandler;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request,
        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(GlobalExceptionHandler.validateErrorsHandler(errors));
        }

        userService.register(request);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.login(request));
    }

    @GetMapping("/logout")
    public void logout() {
        userService.logout();
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request){
        return ResponseEntity.ok(userService.follow(request));
    }

    @DeleteMapping("/follow")
    public void followCancel(@RequestBody FollowRequest request){
        userService.followCancel(request);
    }

}
