package kr.co.writenow.writenow.repository.post;

import kr.co.writenow.writenow.domain.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
