package kr.mashup.udada.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Component
public abstract class Jwt<E> {

    @Value("${jwt.secret-key}")
    protected String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public abstract String createToken(E info);
    public abstract E getInfoFromToken(String token);
}
