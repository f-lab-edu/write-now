package kr.co.writenow.writenow.repository.feed;

import static kr.co.writenow.writenow.domain.feed.QFeed.feed;
import static kr.co.writenow.writenow.domain.post.QPostImage.postImage;
import static kr.co.writenow.writenow.domain.post.QPostTag.postTag;
import static kr.co.writenow.writenow.domain.tag.QTag.tag;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
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
        .select(
            Projections.constructor(FeedProjection.class, feed.feedNo, feed.postNo, feed.writerId,
                feed.writerNickname, feed.content, feed.likeCount, feed.writeDateTime,
                Expressions.as(
                    JPAExpressions.select(
                            Expressions.stringTemplate("group_concat({0})", postImage.filePath))
                        .from(postImage)
                        .where(postImage.post.postNo.eq(feed.postNo))
                        .groupBy(postImage.post.postNo),
                    "imagePaths"),
                Expressions.as(
                    JPAExpressions.select(
                            Expressions.stringTemplate("group_concat({0})", tag.content))
                        .from(tag)
                        .join(postTag)
                        .on(tag.tagNo.eq(postTag.tag.tagNo))
                        .where(postTag.post.postNo.eq(feed.postNo))
                        .groupBy(postTag.post.postNo), "tags")
            ))
        .where(feed.feedUserNo.eq(userNo), lowerThan(lastFeedNo))
        .limit(pageSize)
        .orderBy(feed.feedNo.desc())
        .fetch();
  }

  private BooleanExpression lowerThan(Long feedNo) {
    return feedNo != null ? feed.feedNo.lt(feedNo) : null;
  }
}
