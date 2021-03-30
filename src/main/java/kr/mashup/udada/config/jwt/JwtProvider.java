package kr.mashup.udada.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    /**
     * 1. JWT 토큰 생성
     * 2. JWT 토큰 Validation Check
     * 3. Authentication 객체 생성
     */
    @Value("${jwt.secret-key}")
    private String secretKey;

    private long tokenValidTime = 30 * 24 * 60 * 60 * 1000L;

    private long refreshTokenValidTime = 365 * 24 * 60 * 60 * 1000L;

    public static final String HEADER_NAME = "Authorization";

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String sub) {
        Claims claims = Jwts.claims().setSubject(sub);
        Date date = new Date();
        return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public String createRefreshToken(String sub) {
        Claims claims = Jwts.claims().setSubject(sub);
        Date date = new Date();
        return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(date.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserSub(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserSub(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME).replace("Bearer", "").trim();
    }

    public boolean validateTokenIssuedDate(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e) {
            return false;
        }
    }
}
