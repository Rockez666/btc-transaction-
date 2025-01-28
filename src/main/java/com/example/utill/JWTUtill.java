package com.example.utill;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtill {
    @Value("${jwt_secret}")
    private String secret;

    public String generateLoginToken(String username, Role role) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", username)
                .withClaim("role", role.getNameOfRole())
                .withIssuedAt(new Date())
                .withIssuer("ROCKEZ")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public Map<String,String> validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("ROCKEZ")
                .build();

        DecodedJWT jwt = verifier.verify(token);

        Map<String, String> claims = new HashMap<>();
        jwt.getClaims().forEach((key, value) -> claims.put(key, value.asString()));

        return  claims;
    }


}
