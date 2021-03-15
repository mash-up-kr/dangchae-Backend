package kr.mashup.udada.user.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.mashup.udada.user.dto.common.CodeValidationDTO;
import kr.mashup.udada.user.dto.request.SignInRequestDTO;
import kr.mashup.udada.user.dto.response.SignInResponseDTO;
import kr.mashup.udada.user.exception.InvalidAppleCodeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.PrivateKey;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("");
        String privateKeyString = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        Reader reader = new StringReader(privateKeyString);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        privateKey = converter.getPrivateKey(object);
    }

    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws InvalidAppleCodeException {
        validateAppleLoginToken(signInRequestDTO);
        return null;
    }

    private void validateAppleLoginToken(SignInRequestDTO signInRequestDTO) throws InvalidAppleCodeException {
        String appleClientSecret = createAppleClientSecret();
        verifyCode(appleClientSecret, signInRequestDTO.getCode());
    }

    private String createAppleClientSecret() {
        return Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", "")
                .setIssuer("")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .setAudience("https://appleid.apple.com")
                .setSubject("")
                .signWith(SignatureAlgorithm.ES256, privateKey).compact();
    }

    private void verifyCode(String clientSecret, String code) throws InvalidAppleCodeException {
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

    private void callVerifyTokenAPI(MultiValueMap<String, String> data) throws InvalidAppleCodeException {
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.postForEntity("https://appleid.apple.com/auth/token", data, String.class);
            log.info("callVerifyTokenAPI response is " + response.getBody());
        } catch (HttpClientErrorException e) {
            log.info("callVerifyTokenAPI response is " + e.getMessage());
            throw new InvalidAppleCodeException();
        }
    }


}
