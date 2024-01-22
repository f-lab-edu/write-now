package kr.co.writenow.writenow.repository.user;

import kr.co.writenow.writenow.domain.user.Follow;
import kr.co.writenow.writenow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(User followerUser, User followeeUser);

    void deleteByFollowerAndFollowee(User followerUser, User followeeUser);
}
