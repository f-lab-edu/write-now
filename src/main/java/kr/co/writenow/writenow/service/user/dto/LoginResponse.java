package kr.co.writenow.writenow.service.user.dto;

public record LoginResponse(
        String userId,
        String nickname,
        String email,

        String token
) {
}
