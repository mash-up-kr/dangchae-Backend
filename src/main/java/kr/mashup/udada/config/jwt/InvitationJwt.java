package kr.mashup.udada.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.mashup.udada.diary.vo.InvitationInfo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class InvitationJwt extends Jwt<InvitationInfo>{

    @Override
    public String createToken(InvitationInfo info) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("inviterId", info.getInviterId());
        claims.put("diaryId", info.getDiaryId());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, super.secretKey)
                .compact();
    }

    @Override
    public InvitationInfo getInfoFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        Long inviterId = claims.get("inviterId", Long.class);
        Long diaryId = claims.get("diaryId", Long.class);
        return new InvitationInfo(inviterId, diaryId);
    }
}
