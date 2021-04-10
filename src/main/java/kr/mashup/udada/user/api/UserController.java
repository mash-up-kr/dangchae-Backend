package kr.mashup.udada.user.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.mashup.udada.config.jwt.JwtProvider;
import kr.mashup.udada.user.dto.request.SignInRequestDTO;
import kr.mashup.udada.user.dto.request.SignUpRequestDTO;
import kr.mashup.udada.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody SignInRequestDTO signInRequestDTO, HttpServletRequest request) throws JsonProcessingException {
        return ResponseEntity.ok(userService.signIn(signInRequestDTO, request));
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestPart("profile") MultipartFile profile, SignUpRequestDTO signUpRequestDTO) throws JsonProcessingException {
        signUpRequestDTO.setProfile(profile);
        return ResponseEntity.ok().body(userService.signUp(signUpRequestDTO));
    }

}
