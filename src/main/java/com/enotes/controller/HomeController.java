package com.enotes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.PasswordResetRequest;
import com.enotes.endpoint.HomeControllerEndpoint;
import com.enotes.service.HomeService;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController implements HomeControllerEndpoint
{
	Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public ResponseEntity<?> verifyUserAccount(Integer uid, String code) throws Exception
	{
		log.info("HomeController : verifyUserAccount : Execution Start");
		Boolean isAccVerified = homeService.verifyAccount(uid, code);
		
		if(isAccVerified)
		{
			return CommonUtil.createBuildResponseMessage("User account verified sucessfully!!!", HttpStatus.OK);
		}
		log.info("HomeController : verifyUserAccount : Execution End");
		return CommonUtil.createErrorResponseMessage("Account could not be verified", HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<?> sendEmailForPasswordReset(String email, HttpServletRequest request) throws Exception
	{
		userService.sendEmailPasswordReset(email, request);
		return CommonUtil.createBuildResponseMessage("Email sent sucessfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> verifyPasswordResetLink(Integer uid, String code) throws Exception
	{
		userService.verifyPasswordResetLink(uid, code);
		return CommonUtil.createBuildResponseMessage("Verification success", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> resetPassword(PasswordResetRequest passwordResetRequest) throws Exception
	{
		userService.resetPassword(passwordResetRequest);
		return CommonUtil.createBuildResponseMessage("Password reset success!!!", HttpStatus.OK);
	}
}
