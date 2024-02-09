package kr.co.writenow.writenow.domain.feed;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kr.co.writenow.writenow.domain.common.BaseEntity;
import kr.co.writenow.writenow.domain.post.Post;
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
  @Column(name = "FEED_NO")
  private Long feedNo;

  @Column(name = "FEED_USER_NO")
  private Long feedUserNo;

  @ManyToOne
  @JoinColumn(name = "POST_NO")
  private Post post;

  public Feed(Long feedUserNo, Post post) {
    this.feedUserNo = feedUserNo;
    this.post = post;
  }

}
