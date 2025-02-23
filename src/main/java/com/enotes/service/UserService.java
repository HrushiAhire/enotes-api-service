package com.enotes.service;

import com.enotes.dto.PasswordChangeRequest;

public interface UserService 
{
	public void changePassword(PasswordChangeRequest passwordChangeRequest);
}
