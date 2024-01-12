package kr.co.writenow.writenow.user;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.user.Gender;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.user.UserRegisterException;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.LoginRequest;
import kr.co.writenow.writenow.service.user.dto.RegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySources(value = {@TestPropertySource("classpath:application.yml"), @TestPropertySource("classpath:application-database.yml")})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 데이터베이스를 쓰지 않기 위한 설정
@Transactional
@ContextConfiguration
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 기능을 테스트 합니다.")
    void 회원가입_테스트() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId", "test@@56", Gender.MALE);
        User user = userService.register(request);

        User savedUser = userRepository.findByEmail(user.getEmail()).get();
        assertEquals("test@test.com", savedUser.getEmail());
        // assertThat(user.getUserNo()).isEqualTo(savedUser.getUserNo());
    }

    @Test
    @DisplayName("이메일 중복 회원가입을 테스트 합니다.")
    void 이메일중복_회원가입_테스트() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId", "test@@56", Gender.MALE);
        User user = userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test@test.com", "test1Nickname", "test1UserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("닉네임 중복 회원가입을 테스트 합니다.")
    void 닉네임중복_회원가입_테스트() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId", "test@@56", Gender.MALE);
        User user = userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test1@test.com", "testNickname", "test1UserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("아이디 중복 회원가입을 테스트 합니다.")
    void 아이디중복_회원가입_테스트() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId", "test@@56", Gender.MALE);
        User user = userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test1@test.com", "test1Nickname", "testUserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("로그인 기능을 테스트 합니다.")
    void 로그인_테스트(){
        Object principal = userService.login(new LoginRequest("test", "test@@56"));
        assertNotNull(principal);
    }

}
