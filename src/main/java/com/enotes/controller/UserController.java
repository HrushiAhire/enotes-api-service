package com.enotes.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.PasswordChangeRequest;
import com.enotes.dto.UserResponse;
import com.enotes.entity.User;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/user")
public class UserController 
{
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile()
	{
		User loggedInUser = CommonUtil.getLoggedInUser();
		UserResponse userResponse = modelMapper.map(loggedInUser, UserResponse.class);
		
		return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
	}
	
	@PostMapping("/change-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest)
	{
		userService.changePassword(passwordChangeRequest);
		
		return CommonUtil.createBuildResponseMessage("Password changed sucessfully!!!", HttpStatus.OK);
	}
}
