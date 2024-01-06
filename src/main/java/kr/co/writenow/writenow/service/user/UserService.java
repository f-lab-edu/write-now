package kr.co.writenow.writenow.service.user;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    public String fetchUser() {
        Optional<User> value = userRepository.findByUserNo(1L);

        if(value.isPresent()){
            User user = value.get();
            return String.format("%s Hello World!!", user.getEmail());
        }
        return "anonymous user";
    }
}
