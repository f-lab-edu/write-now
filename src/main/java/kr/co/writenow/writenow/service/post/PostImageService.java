package kr.co.writenow.writenow.service.post;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.post.PostImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostImageService {

  public List<PostImage> makePostImageList(Post post, List<File> files, String s3FilePath) {
    if (files.isEmpty()) {
      return Collections.emptyList();
    }
    List<PostImage> postImages = new ArrayList<>();
    for (File file : files) {
      postImages.add(
          new PostImage(post, file.getName(), String.join("/", s3FilePath, file.getName())));
    }
    return postImages;
  }
}
