package kr.mashup.udada.user.dto.response;

import kr.mashup.udada.user.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignUpResponseDTO {
    private final String token;
    private final String refreshToken;
    private final String nickname;
    private final String profileURL;

    public static SignUpResponseDTO fromEntity(User user) {
        return new SignUpResponseDTO(user.getToken(), user.getRefreshToken(), user.getNickname(), user.getProfile());
    }
}
