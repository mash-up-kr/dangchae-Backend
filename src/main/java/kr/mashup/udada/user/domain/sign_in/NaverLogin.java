package kr.mashup.udada.user.domain.sign_in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.mashup.udada.user.dto.sns.KakaoIDDTO;
import kr.mashup.udada.user.dto.sns.NaverIDDTO;
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
public class NaverLogin implements SnsLogin{

    @Value("${naver.profile.url}")
    private String profileURL;

    @Value("${naver.client.id}")
    private String clientID;

    @Value("${naver.client.secret}")
    private String clientSecret;

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
            headers.set("X-Naver-Client-Id", clientID);
            headers.set("X-Naver-Client-Secret", clientSecret);
            ResponseEntity<String> response = restTemplate.exchange(profileURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            NaverIDDTO naverIDDTO = objectMapper.readValue(response.getBody(), NaverIDDTO.class);
            return String.valueOf(naverIDDTO.getId());
        } catch (HttpClientErrorException e) {
            log.info("callVerifyTokenAPI response is " + e.getMessage());
            throw new InvalidTokenException();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InvalidTokenException();
        }
    }
}
