package com.aburakkontas.manga_payment.infrastructure.security;

import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class SecurityGuards {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtAuthEntryPoint authEntryPoint;

    public SecurityGuards(HttpServletRequest request, HttpServletResponse response, JwtAuthEntryPoint authEntryPoint) {
        this.request = request;
        this.response = response;
        this.authEntryPoint = authEntryPoint;
    }

    public boolean hasTokenGuard() throws IOException {
        var token = extractToken();
        return StringUtils.hasText(token);
    }

    public boolean isValidToken() throws IOException {
        var exception = new ExceptionWithErrorCode("Unauthorized", HttpServletResponse.SC_UNAUTHORIZED);
        authEntryPoint.commence(request, response, exception);
        return false;
    }

    public String extractToken() {
        var bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
