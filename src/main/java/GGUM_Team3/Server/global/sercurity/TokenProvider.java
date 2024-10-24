package GGUM_Team3.Server.global.sercurity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import GGUM_Team3.Server.domain.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String create(UserEntity userEntity) {
        Date expireDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userEntity.getId())
                .setIssuer("oneul")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String create(String userId) {
        Date expireDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userId)
                .setIssuer("oneul")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}