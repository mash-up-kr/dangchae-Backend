package kr.mashup.udada.user.domain;

import kr.mashup.udada.config.jwt.JwtProvider;
import kr.mashup.udada.user.common.Vendor;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String nickname;
    private String profile;
    private String email;

    @CreatedDate
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    private Vendor vendor;

    // SNS 유저 고유 식별자
    private String username;

    // 우리 서비스에서 만들어준 JWT Token
    private String token;
    // 우리 서비스에서 만들어준 JWT Refresh Token
    private String refreshToken;

    @Builder
    public User(String nickname, String profile, String email, Vendor vendor, String username) {
        this.nickname = nickname;
        this.profile = profile;
        this.email = email;
        this.vendor = vendor;
        this.username = username;
    }

    public void updateToken(JwtProvider jwtProvider) {
        String id = String.valueOf(this.id);
        this.token = jwtProvider.createToken(id);
        this.refreshToken = jwtProvider.createRefreshToken(id);
    }
}
