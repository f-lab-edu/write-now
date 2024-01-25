package kr.co.writenow.writenow.service.user.dto;

public record FollowResponse(
        String followerUserId,
        String followeeUserId
) {
}
