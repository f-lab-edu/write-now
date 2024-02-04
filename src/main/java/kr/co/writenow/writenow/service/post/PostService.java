package kr.co.writenow.writenow.service.post;

import jakarta.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.writenow.writenow.common.file.FileService;
import kr.co.writenow.writenow.common.file.FileUtil;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.repository.post.PostRepository;
import kr.co.writenow.writenow.service.feed.FeedService;
import kr.co.writenow.writenow.service.post.dto.PostResponse;
import kr.co.writenow.writenow.service.post.dto.PostWriteRequest;
import kr.co.writenow.writenow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@Validated
public class PostService {

    private final PostRepository postRepository;
    private final PostTagService postTagService;
    private final PostImageService postImageService;
    private final FileUtil fileUtil;
    private final FileService fileService;
    private final UserService userService;
    private final FeedService feedService;
    private final JmsTemplate jmsTemplate;
    private static final String S3_POST_DIR = "post";

    public PostResponse writePost(@Valid PostWriteRequest request, String userId) {
        // 글 저장
        User user = userService.fetchUserByUserId(userId);
        Post post = new Post(user, request.getContent(), request.getCategoryCode());
        post = postRepository.save(post);

        // 파일 업로드
        if (!ObjectUtils.isEmpty(request.getFiles())) {
            List<File> files = makeFiles(request.getFiles());
            String customFilePath = String.join("/", S3_POST_DIR, String.valueOf(post.getPostNo()));
            post.addPostImages(postImageService.makePostImageList(post, files, customFilePath));
            fileService.upload(files, customFilePath);
        }

        //글의 태그, 이미지 저장
        post.addPostTags(postTagService.makePostTagSet(post, request.getTagValues()));

        // 글 작성 이벤트 날리기
        jmsTemplate.convertAndSend("post", post.getPostNo());

        //feedService.save(post);

        return new PostResponse(post);
    }

    private List<File> makeFiles(List<MultipartFile> multipartFiles) {
        List<File> files = new ArrayList<>();
        for (MultipartFile file : multipartFiles) {
            Optional<File> maybeFile = fileUtil.makeFile(file);
            maybeFile.ifPresent(files::add);
        }
        return files;
    }
}
