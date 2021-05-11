package com.project.triport.config;

import com.project.triport.jwt.JwtAccessDeniedHandler;
import com.project.triport.jwt.JwtAuthenticationEntryPoint;
import com.project.triport.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트를 위해 관련 API 모두 허용(ignore)
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
                // CSRF 설정 Disable
        http.csrf().disable()

                // exception handlingd에 직접 만든 401, 403 class 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // Spring Security는 기본적으로 session 사용
                // but 본 로직에서는 session을 사용하지 않으므로 STATELESS 처리
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인&회원가입 API만 token 없이도 요청할 수 있도록 permitAll
                // 나머지는 모두 token 인증 필요
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/api/all/**").permitAll()
                .antMatchers("/mail/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .anyRequest().authenticated()

                // TODO: 빼도 되는지 테스트 필요
                .and()
                .cors()

                // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용
                // Security 최전선에 JwtFilter가 있도록 (?)
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}
