package kr.co.writenow.writenow.domain.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.co.writenow.writenow.common.DateUtil;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FEED")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long feedNo;

  @Column(name = "FEED_USER_NO")
  private Long feedUserNo;

  @Column(name = "POST_NO")
  private Long postNo;

  @Column(name = "WRITER_ID")
  private String writerId;

  @Column(name = "WRITER_NICKNAME")
  private String writerNickname;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "LIKE_COUNT")
  private Integer likeCount;

  @Column(name = "WRITE_DATETIME")
  private String writeDateTime;

  public Feed(Long feedUserNo, Post post, User writer) {
    this.feedUserNo = feedUserNo;
    this.postNo = post.getPostNo();
    this.writerId = writer.getUserId();
    this.writerNickname = writer.getNickname();
    this.content = post.getContent();
    this.likeCount = post.getLikeCount();
    DateUtil.datetimeConvertToString(post.getCreatedDatetime()).ifPresent(datetime -> {
      this.writeDateTime = datetime;
    });
  }

}
