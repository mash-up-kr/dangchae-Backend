package kr.mashup.udada.user.api;

import kr.mashup.udada.user.dto.request.SignInRequestDTO;
import kr.mashup.udada.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok(userService.signIn(signInRequestDTO));
    }

}
