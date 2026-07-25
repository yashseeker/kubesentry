package com.yash.kubesentry.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;


@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername()).issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + expiration)).signWith(getSigningKey()).compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
       return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        Date expiration = extractExpiration((token));
//        Date now = new Date();
//        if(expiration.before(now)){
//            System.out.println("Token Expired");
//            return true;
//        }else{
//            return false;
//        }
        return (expiration.before(new Date()));
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
//        if(extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token)){
//            return true;
//        }else{
//            return false;
//        }
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

//    Header
//    Payload
//    Signature
}
