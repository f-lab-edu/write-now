package kr.co.writenow.writenow.service.post;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.post.PostTag;
import kr.co.writenow.writenow.domain.tag.Tag;
import kr.co.writenow.writenow.repository.tag.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class PostTagService {

    private final TagRepository tagRepository;

    public Set<PostTag> makePostTagSet(Post post, List<String> tagValues){
        if(ObjectUtils.isEmpty(tagValues)){
            return Collections.emptySet();
        }
        Set<PostTag> postTagSet = new HashSet<>();

        for(String value : tagValues){
            Optional<Tag> maybeTag = tagRepository.findByContent(value);
            if(maybeTag.isPresent()){
                postTagSet.add(new PostTag(maybeTag.get(), post));
            }else{
                Tag tag = tagRepository.save(new Tag(value));
                postTagSet.add(new PostTag(tag, post));
            }
        }
        return postTagSet;
    }
}
