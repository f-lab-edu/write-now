package kr.co.writenow.writenow.repository.tag;

import kr.co.writenow.writenow.domain.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByContent(String content);
}
