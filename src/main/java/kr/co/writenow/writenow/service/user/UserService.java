package kr.co.writenow.writenow.service.user;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.user.Gender;
import kr.co.writenow.writenow.domain.user.Role;
import kr.co.writenow.writenow.domain.user.RoleType;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.UserRegisterException;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    /**
     *
     * @param request email, nickname, userId, password, gender
     * @return new user
     */
    public User register(RegisterRequest request) {

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

        return userRepository.save(user);
    }

    private void validate(RegisterRequest request){
        Optional<User> byEmail = userRepository.findByEmail(request.getEmail());

        if(byEmail.isPresent()){
            throw new UserRegisterException(HttpStatus.BAD_REQUEST, "중복되는 이메일입니다.");
        }

        Optional<User> byNickName = userRepository.findByNickname(request.getNickname());

        if(byNickName.isPresent()){
            throw new UserRegisterException(HttpStatus.BAD_REQUEST,"중복되는 닉네임입니다.");
        }

        Optional<User> byUserId = userRepository.findByUserId(request.getUserId());

        if(byUserId.isPresent()){
            throw new UserRegisterException(HttpStatus.BAD_REQUEST, "중복되는 아이디입니다.");
        }
    }
}
