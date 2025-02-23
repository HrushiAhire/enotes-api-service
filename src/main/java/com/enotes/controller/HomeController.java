package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.PasswordResetRequest;
import com.enotes.service.HomeService;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController 
{
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception
	{
		Boolean isAccVerified = homeService.verifyAccount(uid, code);
		
		if(isAccVerified)
		{
			return CommonUtil.createBuildResponseMessage("User account verified sucessfully!!!", HttpStatus.OK);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Account could not be verified", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception
	{
		userService.sendEmailPasswordReset(email, request);
		return CommonUtil.createBuildResponseMessage("Email sent sucessfully", HttpStatus.OK);
	}
	
	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception
	{
		userService.verifyPasswordResetLink(uid, code);
		return CommonUtil.createBuildResponseMessage("Verification success", HttpStatus.OK);
	}
	
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception
	{
		userService.resetPassword(passwordResetRequest);
		return CommonUtil.createBuildResponseMessage("Password reset success!!!", HttpStatus.OK);
	}
}
