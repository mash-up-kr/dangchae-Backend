package kr.mashup.udada.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.mashup.udada.config.jwt.JwtProvider;
import kr.mashup.udada.user.domain.sign_in.KakaoLogin;
import kr.mashup.udada.user.common.Vendor;
import kr.mashup.udada.user.domain.User;
import kr.mashup.udada.user.domain.UserRepository;
import kr.mashup.udada.user.domain.sign_in.AppleLogin;
import kr.mashup.udada.user.domain.sign_in.NaverLogin;
import kr.mashup.udada.user.domain.sign_in.SnsLogin;
import kr.mashup.udada.user.dto.request.SignInRequestDTO;
import kr.mashup.udada.user.dto.request.SignUpRequestDTO;
import kr.mashup.udada.user.dto.response.SignInResponseDTO;
import kr.mashup.udada.user.exception.InvalidVendorException;
import kr.mashup.udada.user.exception.NeedSignUpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final AppleLogin appleLogin;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final NaverLogin naverLogin;
    private final KakaoLogin kakaoLogin;

    @Transactional
    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO) throws JsonProcessingException {
        validateToken(signInRequestDTO.getVendor(), signInRequestDTO.getToken());
        User findUser = getUserInfo(signInRequestDTO);
        findUser.updateToken(jwtProvider);
        return SignInResponseDTO.builder().token(findUser.getToken()).build();
    }

    private User getUserInfo(SignInRequestDTO signInRequestDTO) throws JsonProcessingException {
        return userRepository.findByUsernameAndVendor(getVendorUsername(signInRequestDTO.getVendor(), signInRequestDTO.getToken()), signInRequestDTO.getVendor()).orElseThrow(NeedSignUpException::new);
    }

    private void validateToken(Vendor vendor, String token) {
        switch (vendor) {
            case APPLE:
                appleLogin.validateLoginToken(token);
                break;
            case NAVER:
                naverLogin.validateLoginToken(token);
                break;
            case KAKAO:
                kakaoLogin.validateLoginToken(token);
                break;

        }
    }

    private String getVendorUsername(Vendor vendor, String token) throws JsonProcessingException {
        switch (vendor) {
            case APPLE:
                return appleLogin.getVendorUsername(token);
            case NAVER:
                return naverLogin.getVendorUsername(token);
            case KAKAO:
                return kakaoLogin.getVendorUsername(token);
            default:
                throw new InvalidVendorException();
        }
    }

    public void signUp(SignUpRequestDTO signUpRequestDTO) throws JsonProcessingException {
        validateToken(signUpRequestDTO.getVendor(), signUpRequestDTO.getToken());
        signUpRequestDTO.setUsername(getVendorUsername(signUpRequestDTO.getVendor(), signUpRequestDTO.getToken()));
        userRepository.save(signUpRequestDTO.toEntity());
    }


}
