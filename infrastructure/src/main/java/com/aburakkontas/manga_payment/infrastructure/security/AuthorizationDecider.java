package com.aburakkontas.manga_payment.infrastructure.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.Base64;

@Component // Add this annotation to enable property injection
public class AuthorizationDecider {

    private static String webhookSecret;
    private static String iyzicoSecretKey;

    @Value("${webhook.header.secret}")
    public void setWebhookSecret(String webhookSecret) {
        AuthorizationDecider.webhookSecret = webhookSecret;
    }

    @Value("${iyzico.secretKey}")
    public void setIyzicoSecretKey(String iyzicoSecretKey) {
        AuthorizationDecider.iyzicoSecretKey = iyzicoSecretKey;
    }

    public static AuthorizationDecision fusion(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext request) {
        var header = request.getRequest().getHeader("X-Webhook-Secret");
        if(header == null) return new AuthorizationDecision(false);

        return new AuthorizationDecision(header.equals(webhookSecret));
    }

    @SneakyThrows
    public static AuthorizationDecision iyzico(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext request) {
        var requestBody = request.getRequest().getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        var objectMapper = new ObjectMapper();
        Map<String, Object> requestBodyMap = objectMapper.readValue(requestBody, new TypeReference<>() {});

        var signature = (String) request.getRequest().getHeader("X-IYZ-SIGNATURE");
        var paymentId = (Integer) requestBodyMap.get("iyziPaymentId");
        var eventType = (String) requestBodyMap.get("iyziEventType");

        if(signature == null || paymentId == null || eventType == null) return new AuthorizationDecision(false);

        var stringToBeHashed = iyzicoSecretKey + eventType + paymentId;

        var hash = sha1Base64(stringToBeHashed);

        if(!hash.equals(signature)) return new AuthorizationDecision(false);

        return new AuthorizationDecision(true);

    }

    private static String sha1Base64(String data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        var sha1String = sha1ToString(sha1Hash);
        return Base64.getEncoder().encodeToString(sha1String.getBytes());
    }

    private static String sha1ToString(byte[] digest) {
        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); // Format each byte as a two-digit hexadecimal value
        }
        return result.toString().toLowerCase(Locale.ROOT); // Convert to lowercase for consistency
    }
}
