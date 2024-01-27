package kr.co.writenow.writenow.post;

import jakarta.transaction.Transactional;
import kr.co.writenow.writenow.domain.post.LikePost;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.repository.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySources(value = {@TestPropertySource("classpath:application.yml")})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// 내장 데이터베이스를 쓰지 않기 위한 설정
@Transactional
@ContextConfiguration
public class PostServiceTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void manytomanyTest(){
        //postRepository.save(new Post());
        Optional<Post> post = postRepository.findById(1L);
        if(post.isPresent()){
            List<LikePost> likePosts = post.get().getLikePosts();
        }
    }
}
