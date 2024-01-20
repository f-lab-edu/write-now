package kr.co.writenow.writenow.service.post.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostWriteRequest {

    @NotEmpty(message = "내용을 적어주세요.")
    @Size(max = 300, message = "글은 300자 이하로 작성해야 합니다.")
    private String content;

    private List<MultipartFile> files;

    @NotEmpty(message = "글의 카테고리를 지정해주세요.")
    private String categoryCode;

    private List<String> tagValues;
}
