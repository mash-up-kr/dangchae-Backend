package kr.mashup.udada.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    /**
     * 보통 JWT토큰은 header에 담아서 요청을 보냄
     * 서버는 헤더를 까서 올바른 JWT를 획득하고
     * 그다음 아래 2가지 수행
     *
     * 1. JWT Validation Check
     * 2. JWT 가 올바르다면 Authentication 객체 SecurityContextHolder 에 넣어줘야해
     * 그럼 UsernamePasswordAuthtenticationFilter 안타니까
     *
     * filter1 -> filter2 -> filter3 ... FilterChaining
     * FilterChain.doFilter(request, response)
     *
     * Request -> 1 times
     * Response -> 1 times
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(((HttpServletRequest)request).getRequestURL());
        String token = jwtProvider.getTokenFromHeader(((HttpServletRequest) request));
        if (token != null && jwtProvider.validateTokenIssuedDate(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token, userDetailsService);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(400);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("utf8");
            httpServletResponse.getWriter().write("비정상 메시지");
            return;
        }

        chain.doFilter(request, response);
    }
}
