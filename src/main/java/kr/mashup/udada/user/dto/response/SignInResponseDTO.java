package kr.mashup.udada.user.dto.response;

import kr.mashup.udada.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignInResponseDTO {
    private final String token;
    private final String refreshToken;
    private final String nickname;
    private final String profileURL;

    public static SignInResponseDTO fromEntity(User user) {
        return new SignInResponseDTO(user.getToken(), user.getRefreshToken(), user.getNickname(), user.getProfile());
    }
}
