package kr.co.writenow.writenow.service.post.dto;

import kr.co.writenow.writenow.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
public class PostResponse {

    private Long postNo;
    private String content;
    private String createdDateTime;

    public PostResponse(Post post){
        this.postNo = post.getPostNo();;
        this.content = post.getContent();
        this.createdDateTime = post.getCreatedDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
