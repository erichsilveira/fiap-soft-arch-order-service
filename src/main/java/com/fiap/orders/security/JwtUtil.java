package com.fiap.orders.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private static final long ACCESS_TOKEN_TTL_MINUTES = 60;
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final String secret_key = "supersecretkey";
    private final JwtParser jwtParser;


    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public JwtToken createToken(String cpf) {
        Claims claims = Jwts.claims().setSubject(cpf);
        claims.put("cpf", cpf);
        Date tokenCreateTime = new Date();
        Date tokenTtl = new Date(
            tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_TTL_MINUTES));
        String jwtToken = Jwts.builder()
            .setClaims(claims)
            .setExpiration(tokenTtl)
            .signWith(SignatureAlgorithm.HS256, secret_key)
            .compact();

        return new JwtToken(jwtToken, tokenTtl.toInstant().toEpochMilli(), cpf);
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    @SneakyThrows
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            throw new Exception("Unable to parse claims");
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }

    public record JwtToken(String token, long exp, String sub){

    }
}
