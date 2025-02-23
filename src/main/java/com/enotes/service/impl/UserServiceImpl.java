package com.enotes.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.enotes.dto.EmailRequest;
import com.enotes.dto.PasswordChangeRequest;
import com.enotes.dto.PasswordResetRequest;
import com.enotes.entity.User;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.repository.UserRepository;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	public void changePassword(PasswordChangeRequest passwordChangeRequest) 
	{
		
		User loggedInUser = CommonUtil.getLoggedInUser();
		
		if(!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), loggedInUser.getPassword()))
		{
			throw new IllegalArgumentException("Old password is incorrect!!!");
		}
		
		String encodedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
		loggedInUser.setPassword(encodedNewPassword);
		userRepository.save(loggedInUser);
	}

	@Override
	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {
		User user = userRepository.findByEmail(email);
		
		if(ObjectUtils.isEmpty(user))
		{
			throw new ResourceNotFoundException("User with this email does not exist");
		}
		
		String passwordResetToken = UUID.randomUUID().toString();
		user.getAccountStatus().setPasswordResetToken(passwordResetToken);
		
		User updateUser = userRepository.save(user);
		
		String url = CommonUtil.getUrl(request);
		sendEmailRequest(updateUser, url);
		
	}

	private void sendEmailRequest(User user, String url) throws Exception {
		String message = "Hii <b>[[username]]</b>!! <br><p>You have requested to reset your password</p><br>"
				+"<br> Click the below link to reset your password <br>"
				+"<a href='[[url]]'>Change my password</a> <br>";
		System.out.println(url);
		message = message.replace("[[username]]", user.getFirstName());
		message = message.replace("[[url]]", url+"/api/v1/home/verify-pswd-link?uid="+user.getId()+"&&code="+user.getAccountStatus().getPasswordResetToken());
		
		EmailRequest emailReq = EmailRequest.builder()
				.to(user.getEmail())
				.title("Reset your password")
				.subject("Reset your password")
				.message(message)
				.build();
			emailService.sendEmail(emailReq);
	}

	@Override
	public void verifyPasswordResetLink(Integer uid, String code) throws Exception {
		
		User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
		
		verifyPasswordResetCode(user.getAccountStatus().getPasswordResetToken(), code);
	}

	private void verifyPasswordResetCode(String existToken, String requestToken) {
		if(StringUtils.hasText(requestToken))
		{
			if(!StringUtils.hasText(existToken))
			{
				throw new IllegalArgumentException("Already password reset");
			}
			if(!existToken.equals(requestToken))
			{
				throw new IllegalArgumentException("Invalid URL");
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid Token");
		}
	}

	@Override
	public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception {
		
		Integer uid = passwordResetRequest.getUid();
		User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("Invalid user"));
		
		String newEncodedPassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
		
		user.setPassword(newEncodedPassword);
		user.getAccountStatus().setPasswordResetToken(null);
		userRepository.save(user);
		
	}	
	
	
	
}
