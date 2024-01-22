package kr.co.writenow.writenow.service.user;

import kr.co.writenow.writenow.config.security.jwt.JwtTokenProvider;
import kr.co.writenow.writenow.domain.user.Follow;
import kr.co.writenow.writenow.domain.user.Role;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.CustomException;
import kr.co.writenow.writenow.exception.user.InvalidUserException;
import kr.co.writenow.writenow.exception.user.UserNotFoundException;
import kr.co.writenow.writenow.exception.user.UserRegisterException;
import kr.co.writenow.writenow.repository.user.FollowRepository;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final FollowRepository followRepository;

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

    public User fetchUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> {
            log.warn("{} 아이디로 조회된 회원정보가 없습니다.", userId);
            return new UserNotFoundException(HttpStatus.BAD_REQUEST, "회원정보가 올바르지 않습니다.");
        });
    }

    public FollowResponse follow(FollowRequest request) {
        User follower = fetchUserByUserId(request.followerUserId());
        User followee = fetchUserByUserId(request.followeeUserId());


        Optional<Follow> maybeFollow = followRepository.findByFollowerAndFollowee(follower, followee);
        if(maybeFollow.isPresent()){
            throw new CustomException(HttpStatus.BAD_REQUEST, "이미 팔로우 하고 있는 회원입니다.");
        }

        Follow follow = new Follow(follower, followee);
        follow = followRepository.save(follow);

        return new FollowResponse(follow.getFollower().getUserId(), follow.getFollowee().getUserId());
    }
}
