package com.enotes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enotes.entity.AccountStatus;
import com.enotes.entity.User;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.exceptions.SuccessException;
import com.enotes.repository.UserRepository;
import com.enotes.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService
{
	@Autowired
	private UserRepository usereRepository;

	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
		
		User user = usereRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid User"));
		
		if(user.getAccountStatus().getVerificationCode() == null)
		{
			throw new SuccessException("Account already verified!");
		}
		
		if(user.getAccountStatus().getVerificationCode().equals(verificationCode))
		{
			AccountStatus status = user.getAccountStatus();
			status.setIsActive(true);
			status.setVerificationCode(null);
			
			usereRepository.save(user);
			return true;
		}
		return false;
	}
	
}
