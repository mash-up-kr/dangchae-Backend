package kr.mashup.udada.config;

import kr.mashup.udada.config.jwt.JwtAuthenticationFilter;
import kr.mashup.udada.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers(
                "/**",
                "/user/**",
                "/h2-console/**",
                "/error",
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Http Basic 을 비활성화 하겠다.
        http.httpBasic().disable();
        // csrf 비활성화
        http.csrf().disable();

        http.formLogin().disable();

        // 요청이 들어온 URL들은 기본적으로 인증이 되어야한다.(인증 == 로그인)
        http.authorizeRequests().anyRequest().authenticated();
        // 이 서비스는 세션이 없다.
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 로그인 말고 JWT 로 사용자 인증하기
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
