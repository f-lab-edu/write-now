package kr.co.writenow.writenow.service.feed;

import java.util.ArrayList;
import java.util.List;
import kr.co.writenow.writenow.domain.feed.Feed;
import kr.co.writenow.writenow.domain.post.Post;
import kr.co.writenow.writenow.domain.user.Follow;
import kr.co.writenow.writenow.domain.user.User;
import kr.co.writenow.writenow.repository.feed.FeedRepository;
import kr.co.writenow.writenow.repository.user.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;
  private final FollowRepository followRepository;

  public void save(Post post, User writer){
    //1. 글쓴이를 팔로우 하고 있는 전체 유저 조회
    List<Follow> follows = followRepository.findByFollowee(writer);
    List<Feed> feeds = new ArrayList<>();

    //2. 팔로우 하고 있는 유저의 피드를 생성
    for (Follow follow : follows) {
      Feed feed = new Feed(follow.getFollower().getUserNo(), post, writer);
      feeds.add(feed);
    }

    // 3. 내 피드에 나와야 하니까 추가
    feeds.add(new Feed(writer.getUserNo(), post, writer));

    feedRepository.saveAll(feeds);
  }

}
