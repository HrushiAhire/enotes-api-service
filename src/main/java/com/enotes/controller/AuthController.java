package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.LoginRequest;
import com.enotes.dto.LoginResponse;
import com.enotes.dto.UserDto;
import com.enotes.endpoint.AuthControllerEndpoint;
import com.enotes.service.AuthService;
import com.enotes.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController implements AuthControllerEndpoint
{
	@Autowired
	private AuthService authService;
	
	@PostMapping("/")
	public ResponseEntity<?> registerUser(UserDto userDto, HttpServletRequest request) throws Exception
	{
		String url = CommonUtil.getUrl(request);
		
		Boolean userRegistered = authService.register(userDto, url);
		
		if(userRegistered)
		{
			return CommonUtil.createBuildResponseMessage("User registered sucessfully", HttpStatus.CREATED);
		}
		else
		{
			return CommonUtil.createBuildResponseMessage("User registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(LoginRequest loginRequest) throws Exception
	{
		LoginResponse loginResponse = authService.login(loginRequest);
		
		if(ObjectUtils.isEmpty(loginResponse))
		{
			return CommonUtil.createErrorResponseMessage("Invalid Credentials", HttpStatus.BAD_REQUEST);
		}
		return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
	}
}
