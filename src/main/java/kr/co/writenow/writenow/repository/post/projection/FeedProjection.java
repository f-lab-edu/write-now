package kr.co.writenow.writenow.repository.post.projection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import kr.co.writenow.writenow.common.DateUtil;
import lombok.Getter;

@Getter
public class FeedProjection {

  private Long postNo;
  private String userId;
  private String userNickname;
  private String content;
  private Integer likeCount;
  private String writeDateTime;
  private List<String> imagePaths;
  private Set<String> tags;


  public FeedProjection(Long postNo, String userId, String userNickname, String content, Integer likeCount,
      LocalDateTime writeDateTime, List<String> imagePaths, Set<String> tags) {
    this.postNo = postNo;
    this.userId = userId;
    this.userNickname = userNickname;
    this.content = content;
    this.likeCount = likeCount;
    this.writeDateTime = DateUtil.datetimeConvertToString(writeDateTime);
    this.imagePaths = imagePaths;
    this.tags = tags;
  }
}
