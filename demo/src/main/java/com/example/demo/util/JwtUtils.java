package com.example.demo.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    @Value("${auth.access-secret-key}")
    private String SECRET_KEY;

    @Value("${auth.refresh-secret-key}")
    private String REFRESH_SECRET_KEY;

    @Value("${auth.access-token-expiration-time-ms}")
    private String ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${auth.refresh-token-expiration-time-ms}")
    private String REFRESH_TOKEN_EXPIRATION_TIME;


    public String extractAccessTokenUsername(String token) {
        return extractClaim(SECRET_KEY, token, Claims::getSubject);
    }

    public String extractRefreshTokenUsername(String token) {
        return extractClaim(REFRESH_SECRET_KEY, token, Claims::getSubject);
    }

    public Date extractExpiration(String key, String token) {
        return extractClaim(key, token, Claims::getExpiration);
    }

    public <T> T extractClaim(String key, String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(key, token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String key, String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String key, String token) {
        return extractExpiration(key, token).before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Map<String, Object> a = new HashMap<>();
        for (GrantedAuthority authority : authorities) {
            a.put("0", authority);
        }
        Map<String, Object> claims = a;

        return createAccessToken(claims, userDetails.getUsername());
    }

    private String createAccessToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(ACCESS_TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Integer.parseInt(REFRESH_TOKEN_EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY).compact();
    }

    public Boolean validateRefreshToken(String token) {
        return validateToken(REFRESH_SECRET_KEY, token);

    }
    public Boolean validateAccessToken(String token) {
        return validateToken(SECRET_KEY, token);
    }

    public Boolean validateToken(String key, String token) { //todo maybe change
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }
}
