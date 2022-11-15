package org.cstp.meowtool.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "cstp.jwt")
public class JwtUtil {
    private long expireInterval; // unit: second
    private String secretKey;
    private String header;
    private String issuer;

    public String issueToken(String username) {
        Date currentTime = new Date();
        Date expireTime = new Date(currentTime.getTime() + expireInterval * 1000);

        Map<String, Object> extraClaims = new HashMap<>();
        // extraClaims.put("name", username);
        // extraClaims.put("role", role);

        return Jwts.builder()
            .setHeaderParam("typ", "JWT")
            .setHeaderParam("alg", "HS512")
            .setSubject(username)
            .setIssuedAt(currentTime)
            .setExpiration(expireTime)
            .setIssuer(issuer)
            .addClaims(extraClaims)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    public Claims getClaims(String jwt) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        } catch (Exception e) {
            return Jwts.claims();
        }
    }

    public boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
