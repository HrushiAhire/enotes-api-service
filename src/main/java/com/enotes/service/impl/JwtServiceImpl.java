package com.enotes.service.impl;

import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.enotes.entity.User;
import com.enotes.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService
{

	private String secretKey = "";
	
	
	
	public JwtServiceImpl() {
		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			
			SecretKey sKey = keyGen.generateKey();
			secretKey = Base64.getEncoder().encodeToString(sKey.getEncoded());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String generateToken(User user) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("role", user.getRole());
		claims.put("accountStatus", user.getAccountStatus().getIsActive());
		
		String token = Jwts.builder()
		.claims().add(claims)
		.subject(user.getEmail())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis()+60*60*60*10))
		.and()
		.signWith(getKey())
		.compact();
		return token;
	}

	private Key getKey() {
		
		byte[] decode = Decoders.BASE64.decode(secretKey);
		
		return Keys.hmacShaKeyFor(decode);
	}

	@Override
	public String extractUserName(String token) {
		Claims claims =  extractAllClaims(token);
		
		String email = claims.getSubject();
		
		return email;
	}

	private Claims extractAllClaims(String token) 
	{
		Claims claims = Jwts.parser()
				.verifyWith(decryptKey(secretKey))
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims;
	}


	private SecretKey decryptKey(String secretKey2) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey2);
		SecretKey hmacShaKeyFor = Keys.hmacShaKeyFor(keyBytes);
		return hmacShaKeyFor;
	}

	@Override
	public Boolean validateToken(String token, UserDetails userDetails) {
		
		//User name extracted from token
		String userName = extractUserName(token);
		
		Boolean isExpired = isTokenExpired(token);
		
		if(userName.equalsIgnoreCase(userDetails.getUsername()) && !isExpired)
		{
			return true;
		}
		
		return false;
	}

	private Boolean isTokenExpired(String token) {
		Claims allClaims = extractAllClaims(token);
		
		Date expiration = allClaims.getExpiration();
		
		return expiration.before(new Date());
	}

	
}
