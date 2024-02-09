package kr.co.writenow.writenow.service.user.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kr.co.writenow.writenow.common.DateUtil;
import kr.co.writenow.writenow.domain.feed.Feed;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.post.PostImage;
import kr.co.writenow.writenow.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedResponse {

  private Long feedNo;
  private String userId;
  private String userNickname;
  private String content;
  private Integer likeCount;
  private String writeDateTime;
  private List<String> imagePath;
  private Set<String> tags;

  public FeedResponse(Feed feed) {
    Post post = feed.getPost();
    User writer = post.getWriter();
    this.feedNo = feed.getFeedNo();
    this.userId = writer.getUserId();
    this.userNickname = writer.getNickname();
    this.content = post.getContent();
    this.likeCount = post.getLikeCount();
    DateUtil.datetimeConvertToString(post.getCreatedDatetime()).ifPresent(datetime -> {
      this.writeDateTime = datetime;
    });

    this.imagePath = post.getPostImages().stream()
        .map(PostImage::getFilePath)
        .collect(Collectors.toList());
    this.tags = post.getTags().stream()
        .map(it-> it.getTag().getContent())
        .collect(Collectors.toSet());
  }
}
