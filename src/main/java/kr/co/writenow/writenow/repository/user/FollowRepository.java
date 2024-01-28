package kr.co.writenow.writenow.repository.user;

import java.util.List;
import java.util.Optional;
import kr.co.writenow.writenow.domain.user.Follow;
import kr.co.writenow.writenow.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerAndFollowee(User followerUser, User followeeUser);
    List<Follow> findByFollowee(User followee);

    void deleteByFollowerAndFollowee(User followerUser, User followeeUser);
}
