package kr.co.writenow.writenow.repository.user;

import kr.co.writenow.writenow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNo(Long userNo);
}
