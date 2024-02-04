package kr.co.writenow.writenow.repository.post.projection;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class FeedProjection {

    private Long feedNo;
    private Long postNo;
    private String userId;
    private String userNickname;
    private String content;
    private Integer likeCount;
    private String writeDateTime;
    private List<String> imagePaths;
    private Set<String> tags;

    public FeedProjection(Long feedNo, Long postNo, String writerId, String writerNickname, String content,
        int likeCount, String writeDateTime, String imagePaths, String tags) {
        this.feedNo = feedNo;
        this.postNo = postNo;
        this.userId = writerId;
        this.userNickname = writerNickname;
        this.content = content;
        this.likeCount = likeCount;
        this.writeDateTime = writeDateTime;
        if(imagePaths != null){
            this.imagePaths = Arrays.stream(imagePaths.split(",")).toList();
        }
        if(tags != null){
            this.tags = Arrays.stream(tags.split(",")).collect(Collectors.toSet());
        }
    }

}
