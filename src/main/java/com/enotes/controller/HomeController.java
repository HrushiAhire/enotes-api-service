package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.service.HomeService;
import com.enotes.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController 
{
	@Autowired
	private HomeService homeService;
	
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
}
