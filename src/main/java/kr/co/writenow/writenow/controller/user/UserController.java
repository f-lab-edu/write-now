package kr.co.writenow.writenow.controller.user;

import jakarta.validation.Valid;
import java.util.List;
import kr.co.writenow.writenow.exception.handler.GlobalExceptionHandler;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.FeedResponse;
import kr.co.writenow.writenow.service.user.dto.FollowRequest;
import kr.co.writenow.writenow.service.user.dto.FollowResponse;
import kr.co.writenow.writenow.service.user.dto.LoginRequest;
import kr.co.writenow.writenow.service.user.dto.LoginResponse;
import kr.co.writenow.writenow.service.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<FollowResponse> follow(@RequestBody FollowRequest request) {
    return ResponseEntity.ok(userService.follow(request));
  }

  @DeleteMapping("/follow")
  public void followCancel(@RequestBody FollowRequest request) {
    userService.followCancel(request);
  }

  @GetMapping("/feed/{userId}")
  public ResponseEntity<List<FeedResponse>> fetchFeed(@PathVariable("userId") String userId,
      @RequestParam(value = "lastFeedNo", required = false) Long lastFeedNo,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(userService.fetchFeed(lastFeedNo, userId, pageable));
  }

}
