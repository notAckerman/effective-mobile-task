package org.example.effectivemobiletask.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractSubject(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder().claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .and()
                .signWith(getSign(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractClaims(token);
        return resolver.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSign())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSign() {
        String SECRET = "OHsqV4+55dt0brgx0BhTqBS9wW+ppQBOL7d+F5YvC5VBYo7MZLERQpH+kp1UZHRE";
        byte[] key = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(key);
    }
}
