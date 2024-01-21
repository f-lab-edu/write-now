package kr.co.writenow.writenow.repository.post;

import kr.co.writenow.writenow.domain.post.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
}
