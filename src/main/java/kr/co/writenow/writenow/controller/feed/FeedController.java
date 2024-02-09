package kr.co.writenow.writenow.controller.feed;

import java.util.List;
import kr.co.writenow.writenow.service.feed.FeedService;
import kr.co.writenow.writenow.service.user.dto.FeedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

  private final FeedService feedService;

  @GetMapping("/{userId}")
  public ResponseEntity<List<FeedResponse>> fetchFeed(@PathVariable("userId") String userId,
      @RequestParam(value = "lastFeedNo", required = false) Long lastFeedNo,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(feedService.fetchFeed(lastFeedNo, userId, pageable));
  }

}
