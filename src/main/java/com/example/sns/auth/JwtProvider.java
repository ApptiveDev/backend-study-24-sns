package com.example.sns.auth;

import com.example.sns.entity.User;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.UnauthorizedException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper;
    private final byte[] secret;
    private final long accessTokenExpirationSeconds;

    public JwtProvider(
            ObjectMapper objectMapper,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-seconds}") long accessTokenExpirationSeconds
    ) {
        this.objectMapper = objectMapper;
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public String createAccessToken(User user) {
        Instant now = Instant.now();

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", user.getId().toString());
        payload.put("username", user.getUsername());
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", now.plusSeconds(accessTokenExpirationSeconds).getEpochSecond());

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String signingInput = encodedHeader + "." + encodedPayload;

        return signingInput + "." + sign(signingInput);
    }

    public Long getUserId(String token) {
        Map<String, Object> payload = parsePayload(token);
        Object subject = payload.get("sub");

        if (subject == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        try {
            return Long.valueOf(subject.toString());
        } catch (NumberFormatException exception) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }

    private Map<String, Object> parsePayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        String signingInput = parts[0] + "." + parts[1];
        String expectedSignature = sign(signingInput);
        if (!MessageDigest.isEqual(expectedSignature.getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }

        try {
            Map<String, Object> payload = objectMapper.readValue(
                    BASE64_URL_DECODER.decode(parts[1]),
                    new TypeReference<>() {
                    }
            );
            Number expiresAt = (Number) payload.get("exp");
            if (expiresAt == null || Instant.now().getEpochSecond() >= expiresAt.longValue()) {
                throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
            }
            return payload;
        } catch (UnauthorizedException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
        }
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to encode JWT.", exception);
        }
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return BASE64_URL_ENCODER.encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to sign JWT.", exception);
        }
    }
}
