package kr.mashup.udada.user.service;

import kr.mashup.udada.exception.ResourceNotFoundException;
import kr.mashup.udada.user.dao.UserRepository;
import kr.mashup.udada.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(ResourceNotFoundException::new);
        if(!user.getUsername().equals(username)) {
            throw new ResourceNotFoundException();
        }

        return org.springframework.security.core.userdetails.User.builder().username(username).password("").roles("").build();
    }

    public User getFromUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(ResourceNotFoundException::new);

        return user;
    }
}
