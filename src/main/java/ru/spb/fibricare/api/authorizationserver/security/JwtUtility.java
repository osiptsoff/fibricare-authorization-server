package ru.spb.fibricare.api.authorizationserver.security;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import ru.spb.fibricare.api.authorizationserver.model.Role;
import ru.spb.fibricare.api.authorizationserver.model.User;

@Component
public class JwtUtility {
    private final SecretKey accessKey;
    private final SecretKey refreshKey;

    @Value("${app.config.security.accessLifespawnSec}")
    @Setter
    private Integer accesLifespawn;
    @Value("${app.config.security.refreshLifespawnSec}")
    @Setter
    @Getter
    private Integer refreshLifespawn;

    public JwtUtility(@Value("${app.config.security.accessSecret}") String base64AccessKey,
            @Value("${app.config.security.refreshSecret}") String base64RefreshKey) {
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64AccessKey));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64RefreshKey));
    }

    public String getAccessKey() {
        return Encoders.BASE64.encode(accessKey.getEncoded());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshKey, refreshLifespawn);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessKey, accesLifespawn);
    }

    public UserDetails parseAndValidateRefreshToken(String refreshToken) throws ExpiredJwtException, JwtException {
        return generateUserDetails(refreshToken, refreshKey);
    }

    public UserDetails parseAndValidateAccessToken(String accessToken) throws ExpiredJwtException, JwtException {
        return generateUserDetails(accessToken, accessKey);
    }

    private UserDetails generateUserDetails(String token, SecretKey key) throws ExpiredJwtException, JwtException {
        Claims claims = parseToken(token, key);

        areClaimsValid(claims);

        User user = new User();
        user.setId(Long.parseLong(claims.getSubject()));
        user.setRoles(claims
            .getAudience()
            .stream()
            .map(a -> {
                Role role = new Role();
                role.setName(a);
                return role;
            })
            .collect(Collectors.toSet())
        );

        return user;
    }

    private String generateToken(UserDetails userDetails, SecretKey key, Integer lifespawn) {
        Date now = new Date();

        return Jwts.builder()
            .issuedAt(now)
            .expiration(new Date(now.getTime() + lifespawn * 1000))
            .subject(userDetails.getUsername())
            .audience()
            .add(userDetails.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toSet()))
            .and()
            .signWith(key)
            .compact();
    }

    private Claims parseToken(String token, SecretKey key) throws ExpiredJwtException, JwtException {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch(ExpiredJwtException e) {
            throw e;
        } catch(JwtException | IllegalArgumentException e) {
            throw new JwtException("Failed to parse token");
        }
    }

    private boolean areClaimsValid(Claims claims) throws JwtException {
        if(claims.getIssuedAt() == null
                || claims.getExpiration() == null
                || claims.getSubject() == null
                || claims.getAudience() == null) {
            throw new JwtException("Token has invalid content");
        }

        return true;
    }
}
