package kr.co.writenow.writenow.service.post.dto;

import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostWriteRequest {

    @Max(value = 300)
    private String content;

    private List<MultipartFile> files;

    private String categoryCode;

    private List<String> tagValues;
}
