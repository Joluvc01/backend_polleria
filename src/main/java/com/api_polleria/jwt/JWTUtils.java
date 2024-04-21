package com.api_polleria.jwt;

import com.api_polleria.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey Key;
    private static final long EXPIRATION_TIME_TOKEN = 43200000;// 12 horas

    public JWTUtils(){
        String secreteString = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateUserToken(UserDetails userDetails){
        return Jwts.builder()
                .subject((userDetails.getUsername()))
                .issuedAt(new Date((System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME_TOKEN))
                .signWith(Key)
                .compact();
    }

    public String generateCustomerToken(Customer customer, UserDetails userDetails){
        Long customerId = customer.getId();

        return Jwts.builder()
                .subject((userDetails.getUsername()))
                .claim("Id", customerId)
                .issuedAt(new Date((System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME_TOKEN))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return  extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
