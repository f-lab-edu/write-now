package kr.co.writenow.writenow.repository.feed;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static com.querydsl.core.group.GroupBy.set;
import static kr.co.writenow.writenow.domain.feed.QFeed.feed;
import static kr.co.writenow.writenow.domain.tag.QTag.tag;
import static kr.co.writenow.writenow.domain.post.QPostImage.postImage;
import static kr.co.writenow.writenow.domain.post.QPostTag.postTag;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

import kr.co.writenow.writenow.repository.post.projection.FeedProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryCustom {

    private final JPAQueryFactory factory;

    public List<FeedProjection> fetchByUserNo(Long userNo, Long lastFeedNo, int pageSize) {
        return factory.from(feed)
            .select(Projections.constructor(FeedProjection.class, feed.feedNo, feed.postNo, feed.writerId,
                feed.writerNickname, feed.content, feed.likeCount, feed.writeDateTime))
            .where(feed.feedUserNo.eq(userNo), lowerThan(lastFeedNo))
            .limit(pageSize)
            .orderBy(feed.feedNo.desc())
            .fetch();
    }

    private BooleanExpression lowerThan(Long feedNo) {
        return feedNo != null ? feed.feedNo.lt(feedNo) : null;
    }
}
