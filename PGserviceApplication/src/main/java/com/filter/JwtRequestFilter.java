package com.filter;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	 public JWTUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			
			try {
				userName = jwtUtil.extractUserName(token);
			}catch(ExpiredJwtException ex) {
				System.out.println("Token expired");
			}catch(Exception e) {
				System.out.println("Token error");
			}
		}
		
		 if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			 if(jwtUtil.validateToken(token, userName)) {
				 System.out.println("token"+token+"UserName"+userName);
				 UsernamePasswordAuthenticationToken authToken =
						 new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
				 authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				 SecurityContextHolder.getContext().setAuthentication(authToken);
			 }
		 }
		
		filterChain.doFilter(request, response);
		
	}
}
