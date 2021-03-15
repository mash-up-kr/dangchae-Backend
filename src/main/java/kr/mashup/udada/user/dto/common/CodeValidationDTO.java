package kr.mashup.udada.user.dto.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CodeValidationDTO {
    private String client_id = "";
    private final String client_secret;
    private final String code;
    private String grant_type = "authorization_code";
    private String redirect_uri = "http://ysjleader.com/intro";

    @Override
    public String toString() {
        return "CodeValidationDTO{" +
                "client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", code='" + code + '\'' +
                ", grant_type='" + grant_type + '\'' +
                ", redirect_uri='" + redirect_uri + '\'' +
                '}';
    }
}
