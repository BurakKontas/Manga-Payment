package com.aburakkontas.manga_payment.infrastructure.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfiguration {

    private final JwtAuthEntryPoint authEntryPoint;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final CacheBodyHttpServletFilter cacheBodyHttpServletFilter;

    @Autowired
    public SecurityConfiguration(JwtAuthEntryPoint authEntryPoint,
                                 JWTAuthenticationFilter jwtAuthenticationFilter,
                                 CacheBodyHttpServletFilter cacheBodyHttpServletFilter) {
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.cacheBodyHttpServletFilter = cacheBodyHttpServletFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/docs"
                        ).permitAll()
                        .requestMatchers("/api/v1/webhook/fusion-**").access(AuthorizationDecider::fusion)
                        .requestMatchers("/api/v1/webhook/iyzico").access(AuthorizationDecider::iyzico)
                        .requestMatchers("/api/v1/payments/get-all").hasAuthority("Admin")
                        .requestMatchers("/api/v1/items/get-**").authenticated()
                        .requestMatchers("/api/v1/items/**").hasAuthority("Admin")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint));


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}