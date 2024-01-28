package kr.co.writenow.writenow.service.user.dto;

import java.util.List;
import java.util.Set;
import kr.co.writenow.writenow.repository.post.projection.FeedProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FeedResponse {

  private Long postNo;
  private String userId;
  private String userNickname;
  private String content;
  private Integer likeCount;
  private String writeDateTime;
  private List<String> imagePath;
  private Set<String> tags;

  public FeedResponse(FeedProjection projection) {
    this.postNo = projection.getPostNo();
    this.userId = projection.getUserId();
    this.userNickname = projection.getUserNickname();
    this.content = projection.getContent();
    this.likeCount = projection.getLikeCount();
    this.writeDateTime = projection.getWriteDateTime();
    this.imagePath = projection.getImagePaths();
    this.tags = projection.getTags();
  }
}
