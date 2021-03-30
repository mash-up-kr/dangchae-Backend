package kr.mashup.udada.user.domain.sign_in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.mashup.udada.user.dto.common.CodeValidationDTO;
import kr.mashup.udada.user.dto.sns.AppleSubDTO;
import kr.mashup.udada.user.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppleLogin implements SnsLogin{

    private final PrivateKey applePrivateKey;

    @Override
    public void validateLoginToken(String token) {
        String appleClientSecret = createAppleClientSecret();
        verifyCode(appleClientSecret, token);
    }

    private String createAppleClientSecret() {
        return Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", "3SVRCM9Z2P")
                .setIssuer("8BQHKQSA4Y")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setAudience("https://appleid.apple.com")
                .setSubject("com.dangchae.udada.web")
                .signWith(SignatureAlgorithm.ES256, applePrivateKey).compact();
    }

    private void verifyCode(String clientSecret, String code) {
        CodeValidationDTO codeValidationDTO = new CodeValidationDTO(clientSecret, code);
        MultiValueMap<String, String> data = prepareVerifyTokenAPIParams(codeValidationDTO);
        callVerifyTokenAPI(data);
    }

    private MultiValueMap<String, String> prepareVerifyTokenAPIParams(CodeValidationDTO codeValidationDTO) {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("client_id", codeValidationDTO.getClient_id());
        data.add("client_secret", codeValidationDTO.getClient_secret());
        data.add("code", codeValidationDTO.getCode());
        data.add("grant_type", codeValidationDTO.getGrant_type());
        data.add("redirect_uri", codeValidationDTO.getRedirect_uri());
        return data;
    }

    private void callVerifyTokenAPI(MultiValueMap<String, String> data) {
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.postForEntity("https://appleid.apple.com/auth/token", data, String.class);
            log.info("callVerifyTokenAPI response is " + response.getBody());
        } catch (HttpClientErrorException e) {
            log.info("callVerifyTokenAPI response is " + e.getMessage());
            throw new InvalidTokenException();
        }
    }

    @Override
    public String getVendorUsername(String idToken) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = new String(Base64.getDecoder().decode(idToken.split("[.]")[1].getBytes()));
        log.info("payload is {}", payload);

        AppleSubDTO appleSubDTO = objectMapper.readValue(payload, AppleSubDTO.class);
        return appleSubDTO.getSub();
    }
}
