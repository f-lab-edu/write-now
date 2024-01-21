package kr.co.writenow.writenow.repository.post;

import kr.co.writenow.writenow.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
