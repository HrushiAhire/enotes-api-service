package com.enotes.exceptions;

public class JwtTokenExpirationException extends RuntimeException
{

	public JwtTokenExpirationException(String message) 
	{
		super(message);
	}

}
