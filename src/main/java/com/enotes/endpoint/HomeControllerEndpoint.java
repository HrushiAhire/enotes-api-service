package com.enotes.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enotes.dto.PasswordResetRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Home", description = "All the Home APIs without Authentication")
@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoint 
{
	@Operation(summary = "Verification of User Account", tags = {"Home"}, description = "Verification of User Account after registration")
	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@Operation(summary = "Send email for password resetting", tags = {"Home"}, description = "Email is sent for password resetting")
	@GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;
	
	@Operation(summary = "Password verification link", tags = {"Home"}, description = "Password verfication")
	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@Operation(summary = "Reset Password", tags = {"Home"}, description = "Reset Password")
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}
