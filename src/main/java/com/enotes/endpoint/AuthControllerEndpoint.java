package com.enotes.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enotes.dto.LoginRequest;
import com.enotes.dto.UserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Authentication", description = "All the User Authentication APIs")
@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoint 
{
	@ApiResponses(value = {
			@ApiResponse (responseCode = "201", description = "Registration Sucessful"),
			@ApiResponse (responseCode = "500", description = "Internal Server Error"),
			@ApiResponse (responseCode = "400", description = "Bad Request")
			})
	@Operation(summary = "User Register Endpoint", tags = {"Authentication", "Home"})
	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception;
	
	@Operation(summary = "User Login Endpoint", tags = {"Authentication", "Home"})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception;
}
