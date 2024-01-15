package kr.co.writenow.writenow.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.user.Gender;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.user.UserRegisterException;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.LoginRequest;
import kr.co.writenow.writenow.service.user.dto.LoginResponse;
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

@SpringBootTest
@TestPropertySources(value = {@TestPropertySource("classpath:application.yml"),
    @TestPropertySource("classpath:application-database.yml")})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 내장 데이터베이스를 쓰지 않기 위한 설정
@Transactional
@ContextConfiguration
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입이 정상적으로 처리됐을 때 DB에서 조회한 정보와 이메일이 동일하다.")
    void successUserRegister() {
        //given
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId",
            "test@@56", Gender.MALE);

        //when
        userService.register(request);

        //then
        User savedUser = userRepository.findByEmail(request.getEmail()).get();
        assertEquals("test@test.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("회원가입 시 이메일이 중복되면 에러를 반환합니다.")
    void emailDuplicateTest() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId",
            "test@@56", Gender.MALE);
        userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test@test.com", "test1Nickname",
                "test1UserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("회원가입 시 닉네임이 중복되면 에러를 반환합니다.")
    void nicknameDuplicateTest() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId",
            "test@@56", Gender.MALE);
        userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test1@test.com", "testNickname",
                "test1UserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("회원가입 시 이메일이 중복되면 에러를 반환합니다.")
    void 아이디중복_회원가입_테스트() {
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId",
            "test@@56", Gender.MALE);
        userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, () -> {
            RegisterRequest newReq = new RegisterRequest("test1@test.com", "test1Nickname",
                "testUserId", "test@@56", Gender.FEMALE);
            userService.register(newReq);
        });
    }

    @Test
    @DisplayName("로그인이 정상적으로 진행됐을 떼 token을 반환합니다.")
    void successLoginTest() {
        userService.register(new RegisterRequest("test@test.com", "testNickname", "test",
            "test@@56", Gender.MALE));
        LoginResponse response = userService.login(new LoginRequest("test", "test@@56"));
        assertNotNull(response.token());
    }

}
