package com.enotes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
	private UserResponse userDto;
	
	private String token;
}
