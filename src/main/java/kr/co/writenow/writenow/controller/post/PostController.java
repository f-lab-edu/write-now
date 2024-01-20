package kr.co.writenow.writenow.controller.post;

import kr.co.writenow.writenow.common.MultipartUtil;
import kr.co.writenow.writenow.service.post.PostService;
import kr.co.writenow.writenow.service.post.dto.PostWriteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final MultipartUtil multipartUtil;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Object> writePost(MultipartHttpServletRequest servletRequest, @PathVariable("userId") String userId){
        PostWriteRequest request = multipartUtil.convertTo(PostWriteRequest.class, servletRequest);
        request.setFiles(servletRequest.getFiles("files"));
        return ResponseEntity.ok(postService.writePost(userId, request));
    }
}
