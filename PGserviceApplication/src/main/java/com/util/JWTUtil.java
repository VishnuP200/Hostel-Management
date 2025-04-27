package com.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	
	private final String secretKey = "7f92b7f5bb8b4acbb9b4d7a68f8f7b30";
	private final long expiration = 1000*60*60;
	private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
	
	public String generateAuthToken(String userName) {
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
