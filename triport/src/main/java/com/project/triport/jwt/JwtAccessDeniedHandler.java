package com.project.triport.jwt;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        // 필요한 권한 없이 접근하는 경우 403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한이 없습니다. 관리자에게 문의하시기 바랍니다.");
    }

}
