package kr.co.writenow.writenow.service.user;

import java.util.Optional;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> byUserId = userRepository.findByUserId(userId);
        return byUserId.orElseThrow(
            () -> new InternalAuthenticationServiceException("아이디와 일치하는 회원이 존재하지 않습니다."));
    }
}
