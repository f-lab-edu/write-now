package kr.co.writenow.writenow.user;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.user.Gender;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.exception.UserRegisterException;
import kr.co.writenow.writenow.repository.user.UserRepository;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.RegisterRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySources(value = {@TestPropertySource("classpath:application.yml"), @TestPropertySource("classpath:application-database.yml")})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 데이터베이스를 쓰지 않기 위한 설정
@Transactional
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
    @DisplayName("중복 회원가입을 테스트 합니다. 이메일, 아이디, 닉네임이 중복되면 예외를 반환합니다.")
    void 중복_회원가입_테스트(){
        RegisterRequest request = new RegisterRequest("test@test.com", "testNickname", "testUserId", "test@@56", Gender.MALE);
        User user = userService.register(request);

        Assertions.assertThrows(UserRegisterException.class, ()-> {
            userService.register(request);
        });
    }
}
