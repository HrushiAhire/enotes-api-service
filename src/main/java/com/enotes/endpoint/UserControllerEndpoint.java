package com.enotes.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enotes.dto.PasswordChangeRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "All the User operation APIs")
@RequestMapping("/api/v1/user")
public interface UserControllerEndpoint 
{
	@Operation(summary = "Get User Profile", tags = {"User"}, description = "Get User's Profile")
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile();
	
	@Operation(summary = "Change User Password", tags = {"User"}, description = "Change User Password")
	@PostMapping("/change-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest);
}
