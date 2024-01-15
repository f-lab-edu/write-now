package kr.co.writenow.writenow.service.user;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import kr.co.writenow.writenow.config.security.jwt.JwtTokenProvider;
import kr.co.writenow.writenow.domain.user.Role;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.user.InvalidUserException;
import kr.co.writenow.writenow.exception.user.UserNotFoundException;
import kr.co.writenow.writenow.exception.user.UserRegisterException;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.dto.LoginRequest;
import kr.co.writenow.writenow.service.user.dto.LoginResponse;
import kr.co.writenow.writenow.service.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    /**
     * @param request email, nickname, userId, password, gender
     * @return new user
     */
    public void register(RegisterRequest request) {

        this.validate(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
            .email(request.getEmail())
            .nickname(request.getNickname())
            .userId(request.getUserId())
            .password(encodedPassword)
            .gender(request.getGender())
            .isLocked(false)
            .loginFailCount(0)
            .roles(Collections.singleton(new Role()))
            .build();

        userRepository.save(user);
    }

    private void validate(RegisterRequest request) {
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());

        if (byEmail.isPresent()) {
            throw new UserRegisterException(HttpStatus.BAD_REQUEST, "중복되는 이메일입니다.");
        }

        Optional<User> byNickName = userRepository.findByNickname(request.getNickname());

        if (byNickName.isPresent()) {
            throw new UserRegisterException(HttpStatus.BAD_REQUEST, "중복되는 닉네임입니다.");
        }

        Optional<User> byUserId = userRepository.findByUserId(request.getUserId());

        if (byUserId.isPresent()) {
            throw new UserRegisterException(HttpStatus.BAD_REQUEST, "중복되는 아이디입니다.");
        }
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authenticate = authenticate(request.userId(), request.password());
        User user = (User) authenticate.getPrincipal();
        String token = tokenProvider.generatedToken(user, Duration.ofDays(15));
        return new LoginResponse(user.getUserId(), user.getNickname(), user.getEmail(), token);
    }

    public Authentication authenticate(String userId, String password) {
        try {
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userId, password));
        } catch (InternalAuthenticationServiceException e) {
            throw new UserNotFoundException(HttpStatus.BAD_REQUEST, "아이디와 일치하는 회원이 없습니다.");
        } catch (DisabledException e) {
            throw new InvalidUserException(HttpStatus.BAD_REQUEST, "로그인 가능한 회원이 아닙니다.");
        } catch (BadCredentialsException e) {
            this.increaseLoginFailCount(userId);
            throw new BadCredentialsException("비밀번호가 틀렸습니다.");
        } catch (LockedException e) {
            throw new LockedException("계정이 잠겨있습니다. 비밀번호 찾기를 진행해주세요.");
        }
    }

    public void increaseLoginFailCount(String userId) {
        Optional<User> byUserId = userRepository.findByUserId(userId);
        if (byUserId.isEmpty()) {
            return;
        }

        User user = byUserId.get();

        if (user.isMaxLoginFailCount()) {
            userRepository.locked(user.getUserNo());
            return;
        }

        userRepository.increaseLoginFailCount(user.getUserNo());
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
