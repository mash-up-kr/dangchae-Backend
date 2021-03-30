package kr.mashup.udada.user.dto.request;

import kr.mashup.udada.user.common.Vendor;
import kr.mashup.udada.user.domain.User;
import lombok.*;

import java.io.File;

@Getter
@RequiredArgsConstructor
public class SignUpRequestDTO {
    private final String nickname;
    private final File profile;
    private final String email;
    private final String code;
    private final String token;
    private final Vendor vendor;

    private String profileURL;
    private String username;

    public User toEntity() {
        return User.builder().username(username).email(email).nickname(nickname).profile(profileURL).vendor(vendor).build();
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
