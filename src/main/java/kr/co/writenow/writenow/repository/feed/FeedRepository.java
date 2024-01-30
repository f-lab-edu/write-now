package kr.co.writenow.writenow.repository.feed;

import kr.co.writenow.writenow.domain.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
