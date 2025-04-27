package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	public JwtRequestFilter jwtRequestFilter;
	
	   @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		   http
           .csrf(csrf -> csrf.disable())
           .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/auth/**").permitAll()  //  allow this without auth for authenticate
               .anyRequest().authenticated());
		   http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }

}
