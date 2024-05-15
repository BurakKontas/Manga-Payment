package com.aburakkontas.manga_payment.infrastructure.security;

import com.aburakkontas.manga_payment.domain.exceptions.ExceptionWithErrorCode;
import com.aburakkontas.manga_payment.domain.primitives.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        var objectMapper = new ObjectMapper();

        var result = Result.failure(authException.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        var json = objectMapper.writeValueAsString(result);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().print(json);
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, ExceptionWithErrorCode exception) throws IOException {
        var objectMapper = new ObjectMapper();

        var result = Result.failure(exception.getMessage(), exception.getCode());
        var json = objectMapper.writeValueAsString(result);

        response.setStatus(exception.getCode());
        response.setContentType("application/json");
        response.getWriter().print(json);
    }
}