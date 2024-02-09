package kr.co.writenow.writenow.repository.feed;

import static kr.co.writenow.writenow.domain.feed.QFeed.feed;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.writenow.writenow.domain.feed.Feed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryCustom {

  private final JPAQueryFactory factory;

  public List<Feed> fetchByUserNo(Long userNo, Long lastFeedNo, int pageSize) {
    return factory.selectFrom(feed)
        .where(feed.feedUserNo.eq(userNo), lowerThan(lastFeedNo))
        .limit(pageSize)
        .orderBy(feed.feedNo.desc())
        .fetch();
  }

  private BooleanExpression lowerThan(Long feedNo) {
    return feedNo != null ? feed.feedNo.lt(feedNo) : null;
  }
}
