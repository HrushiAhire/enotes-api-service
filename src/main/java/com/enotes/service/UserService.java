package com.enotes.service;

import com.enotes.dto.PasswordChangeRequest;
import com.enotes.dto.PasswordResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService 
{
	public void changePassword(PasswordChangeRequest passwordChangeRequest);

	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

	public void verifyPasswordResetLink(Integer uid, String code) throws Exception;

	public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;
}
