package kr.co.writenow.writenow.repository.post;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;
import static com.querydsl.core.types.Projections.list;
import static kr.co.writenow.writenow.domain.post.QPost.post;
import static kr.co.writenow.writenow.domain.post.QPostImage.postImage;
import static kr.co.writenow.writenow.domain.post.QPostTag.postTag;
import static kr.co.writenow.writenow.domain.tag.QTag.tag;
import static kr.co.writenow.writenow.domain.user.QFollow.follow;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.co.writenow.writenow.repository.post.projection.FeedProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public List<FeedProjection> fetchFeed(Long lastPostNo, String userId, Pageable pageable) {
    return queryFactory
        .selectFrom(post)
        .leftJoin(postImage).on(post.postNo.eq(postImage.post.postNo))
        .leftJoin(postTag).on(post.postNo.eq(postTag.post.postNo))
        .leftJoin(tag).on(postTag.tag.tagNo.eq(tag.tagNo))
        .where(
            lowerThan(lastPostNo),
            post.writer.userId.eq(userId).or(
            post.writer.userNo.in(
                JPAExpressions.select(follow.followee.userNo)
                    .from(follow)
                    .where(follow.follower.userId.eq(userId))
            )
        ))
        .limit(pageable.getPageSize())
        .orderBy(post.postNo.desc())
        .transform(
            groupBy(post.postNo).list(
                Projections.constructor(FeedProjection.class, post.postNo, post.writer.userId,
                    post.writer.nickname, post.content, post.likeCount, post.createdDatetime,
                    list(postImage.filePath), set(tag.content)))
        );
  }

  private Predicate lowerThan(Long lastPostNo) {
    return lastPostNo != null ? post.postNo.lt(lastPostNo) : null;
  }

}

