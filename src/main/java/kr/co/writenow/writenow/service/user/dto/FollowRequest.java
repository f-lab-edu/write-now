package kr.co.writenow.writenow.service.user.dto;

public record FollowRequest(
        String followerUserId,
        String followeeUserId
) {
}
