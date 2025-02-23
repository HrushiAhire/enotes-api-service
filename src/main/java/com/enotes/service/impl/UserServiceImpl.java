package com.enotes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.enotes.dto.PasswordChangeRequest;
import com.enotes.entity.User;
import com.enotes.repository.UserRepository;
import com.enotes.service.UserService;
import com.enotes.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
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
}
