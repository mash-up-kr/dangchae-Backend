package kr.mashup.udada.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.mashup.udada.config.jwt.JwtProvider;
import kr.mashup.udada.exception.ResourceNotFoundException;
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
import kr.mashup.udada.user.dto.response.SignUpResponseDTO;
import kr.mashup.udada.user.exception.InvalidVendorException;
import kr.mashup.udada.user.exception.NeedSignUpException;
import kr.mashup.udada.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final AppleLogin appleLogin;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final NaverLogin naverLogin;
    private final KakaoLogin kakaoLogin;
    private final S3Util s3Util;
    private final String IMAGE_DIR = "profile";

    @PostConstruct
    public void init() {
        userRepository.save(User.builder().username("dangchae0").vendor(Vendor.KAKAO).email("ysjleader@gmail.com").build());
        userRepository.save(User.builder().username("dangchae1").vendor(Vendor.KAKAO).email("ysjleader@gmail.com").build());
    }

    @Transactional
    public SignInResponseDTO signIn(SignInRequestDTO signInRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        User findUser;
        if(request.getHeader(JwtProvider.HEADER_NAME) != null) {
            Long id = Long.valueOf(jwtProvider.getInfoFromToken(jwtProvider.getTokenFromHeader(request)));
            findUser = userRepository.findById(id).orElseThrow(NeedSignUpException::new);
        } else {
            validateToken(signInRequestDTO.getVendor(), signInRequestDTO.getToken());
            findUser = getUserInfo(signInRequestDTO);
            findUser.updateToken(jwtProvider);
        }
        return SignInResponseDTO.fromEntity(findUser);
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

    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) throws JsonProcessingException {
        validateToken(signUpRequestDTO.getVendor(), signUpRequestDTO.getToken());
        signUpRequestDTO.setProfileURL(s3Util.upload(IMAGE_DIR, signUpRequestDTO.getProfile(), signUpRequestDTO.getNickname()));
        signUpRequestDTO.setUsername(getVendorUsername(signUpRequestDTO.getVendor(), signUpRequestDTO.getToken()));
        User user = userRepository.save(signUpRequestDTO.toEntity());
        user.updateToken(jwtProvider);
        return SignUpResponseDTO.fromEntity(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long id = Long.valueOf(username);
        User user = userRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        if(user.getId() != id) {
            throw new ResourceNotFoundException();
        }

        return org.springframework.security.core.userdetails.User.builder().username(username).password("").roles("").build();
    }

    public User getFromUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        String username = userDetails.getUsername();
        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(ResourceNotFoundException::new);

        return user;
    }



}
