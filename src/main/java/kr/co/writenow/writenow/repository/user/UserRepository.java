package kr.co.writenow.writenow.repository.user;

import kr.co.writenow.writenow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNo(Long userNo);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByUserId(String userId);

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 예외 발생 시 동작하므로 transaction을 분리
    @Modifying
    @Query(value = "update USER set LOGIN_FAIL_COUNT = LOGIN_FAIL_COUNT + 1 where USER_NO = :userNo", nativeQuery = true)
    void increaseLoginFailCount(@Param("userNo") Long userNo);

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 예외 발생 시 동작하므로 transaction을 분리
    @Modifying
    @Query(value = "update USER set IS_LOCKED = TRUE where USER_NO = :userNo", nativeQuery = true)
    void locked(@Param("userNo") Long userNo);
}
