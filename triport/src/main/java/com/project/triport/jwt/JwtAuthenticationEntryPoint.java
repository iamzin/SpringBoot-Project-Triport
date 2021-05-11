package com.project.triport.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
            throws IOException {
        // 유효한 자격증명을 제공하지 않고 접근하는 경우 401
        // 계정 정보 = token
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "계정 정보가 잘못됐거나, 로그인이 필요합니다.");
    }
}
