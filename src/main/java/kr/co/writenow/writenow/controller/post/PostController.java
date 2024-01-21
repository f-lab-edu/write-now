package kr.co.writenow.writenow.controller.post;

import kr.co.writenow.writenow.exception.handler.GlobalExceptionHandler;
import kr.co.writenow.writenow.service.post.PostService;
import kr.co.writenow.writenow.service.post.dto.PostWriteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<Object> writePost(@RequestParam(value = "files") List<MultipartFile> files, @RequestPart(value = "data") PostWriteRequest request, @PathVariable("userId") String userId, Errors errors) {
        if(errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GlobalExceptionHandler.validateErrorsHandler(errors));
        }
        request.setFiles(files);
        return ResponseEntity.ok(postService.writePost(request, userId));
    }
}
