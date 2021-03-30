package kr.mashup.udada.user.domain.sign_in;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SnsLogin {
    void validateLoginToken(String token);
    String getVendorUsername(String idToken) throws JsonProcessingException;
}
