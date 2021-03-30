package kr.mashup.udada.user.dto.request;

import kr.mashup.udada.user.common.Vendor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequestDTO {
    private Vendor vendor;
    private String code;
    private String token;
}
