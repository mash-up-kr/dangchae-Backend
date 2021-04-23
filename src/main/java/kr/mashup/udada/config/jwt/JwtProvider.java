package kr.mashup.udada.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider extends Jwt<String> {

    /**
     * 1. JWT 토큰 생성
     * 2. JWT 토큰 Validation Check
     * 3. Authentication 객체 생성
     */
    private long tokenValidTime = 30 * 24 * 60 * 60 * 1000L;

    public static final String HEADER_NAME = "Authorization";

    private final UserDetailsService userDetailsService;

    @Override
    public String createToken(String sub) {
        Claims claims = Jwts.claims().setSubject(sub);
        Date date = new Date();
        return Jwts.builder().setClaims(claims).setIssuedAt(date).setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getInfoFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String getInfoFromToken(String token) {
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
