package com.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expiration;

	
	 private Key key;
	 
	@PostConstruct
    public void init() {
        System.out.println("Injected secret key: " + secretKey);
        System.out.println("Injected expiration: " + expiration);
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }
	
	public String generateAuthToken(String userName) {
		System.out.println("secretkey"+secretKey);
		return Jwts.builder().
				setSubject(userName).
				setIssuedAt(new Date())
				.setExpiration(new Date(expiration + System.currentTimeMillis()))
				.signWith(key, SignatureAlgorithm.HS256).compact();
		
	}
	
	public boolean validateToken(String token, String UserName) {
		return (UserName.equals(extractUserName(token)) && !isTokenExpired(token));
		
	}
	
	public Claims extract(String token) {
		return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody();
	}
	
	public String extractUserName(String token) {
		String userName = extract(token).getSubject();
		return userName;
	}
	
	public boolean isTokenExpired(String token) {
		Date d = extract(token).getExpiration();
		return d.before(new Date());
	}

}
