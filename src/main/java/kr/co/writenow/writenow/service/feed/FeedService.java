package kr.co.writenow.writenow.service.feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.co.writenow.writenow.domain.feed.Feed;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.user.Follow;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.repository.feed.FeedRepository;
import kr.co.writenow.writenow.repository.feed.FeedRepositoryCustom;
import kr.co.writenow.writenow.repository.post.PostRepository;
import kr.co.writenow.writenow.repository.post.projection.FeedProjection;
import kr.co.writenow.writenow.repository.user.FollowRepository;
import kr.co.writenow.writenow.service.user.UserService;
import kr.co.writenow.writenow.service.user.dto.FeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final FeedRepositoryCustom feedRepositoryCustom;

    @JmsListener(destination = "post", containerFactory = "myFactory")
    public void save(Long postNo) {
        Optional<Post> maybePost = postRepository.findById(postNo);
        maybePost.ifPresent(post -> {
            User writer = post.getWriter();
            //1. 글쓴이를 팔로우 하고 있는 전체 유저 조회
            List<Follow> follows = followRepository.findByFollowee(writer);
            List<Feed> feeds = new ArrayList<>();

            //2. 팔로우 하고 있는 유저의 피드를 생성
            for (Follow follow : follows) {
                Feed feed = new Feed(follow.getFollower().getUserNo(), post);
                feeds.add(feed);
            }

            // 3. 내 피드에 나와야 하니까 추가
            feeds.add(new Feed(writer.getUserNo(), post));

            feedRepository.saveAll(feeds);
        });
    }

    public List<FeedResponse> fetchFeed(Long lastFeedNo, String userId, Pageable pageable) {
        User user = userService.fetchUserByUserId(userId);
        List<FeedProjection> feeds = feedRepositoryCustom
            .fetchByUserNo(user.getUserNo(), lastFeedNo, pageable.getPageSize());
        return feeds.stream().map(FeedResponse::new).toList();
    }

}
