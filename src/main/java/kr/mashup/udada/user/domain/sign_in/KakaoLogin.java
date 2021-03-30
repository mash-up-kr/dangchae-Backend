package kr.mashup.udada.user.domain.sign_in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mashup.udada.user.dto.sns.KakaoIDDTO;
import kr.mashup.udada.user.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class KakaoLogin implements SnsLogin{

    @Value("${kakao.profile.url}")
    private String profileURL;

    private final ObjectMapper objectMapper;

    @Override
    public void validateLoginToken(String token) {

    }

    @Override
    public String getVendorUsername(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + token);
            ResponseEntity<String> response = restTemplate.exchange(profileURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            KakaoIDDTO kakaoIDDTO = objectMapper.readValue(response.getBody(), KakaoIDDTO.class);
            return String.valueOf(kakaoIDDTO.getId());
        } catch (HttpClientErrorException e) {
            log.info("callVerifyTokenAPI response is " + e.getMessage());
            throw new InvalidTokenException();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InvalidTokenException();
        }
    }
}
