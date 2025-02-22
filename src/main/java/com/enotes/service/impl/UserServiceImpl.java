package com.enotes.service.impl;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.config.security.CustomUserDetails;
import com.enotes.dto.EmailRequest;
import com.enotes.dto.LoginRequest;
import com.enotes.dto.LoginResponse;
import com.enotes.dto.UserDto;
import com.enotes.entity.AccountStatus;
import com.enotes.entity.Role;
import com.enotes.entity.User;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;
import com.enotes.service.UserService;
import com.enotes.util.Validation;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private Validation validation;
	
	@Autowired
	private EmailService emailSender;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtServiceImpl jwtServiceImpl;
	
	@Override
	public Boolean register(UserDto userdto, String url) throws Exception {
		
		//validate the user role
		validation.roleValidation(userdto);
		
		User user = modelMapper.map(userdto, User.class);
		
		AccountStatus status = AccountStatus.builder()
				.isActive(false)
				.verificationCode(UUID.randomUUID().toString())
				.build();
		
		user.setAccountStatus(status);
		user.setPassword(passwordEncoder.encode(userdto.getPassword()));
		setRole(userdto, user);
		
		User savedUser = userRepository.save(user);
		
		if(!ObjectUtils.isEmpty(savedUser))
		{
			//send email
			emailSend(savedUser, url);
			return true;
		}
		return false;
	}

	private void emailSend(User savedUser, String url) throws Exception {
		String message = "Hii <b>[[username]]</b>!! Your account registered sucessfully"
				+"<br> Click the below link to verify your account <br>"
				+"<a href='[[url]]'>Click Here</a> <br>";
		
		message = message.replace("[[username]]", savedUser.getFirstName());
		message = message.replace("[[url]]", url+"/api/v1/home/verify?uid="+savedUser.getId()+"&&code="+savedUser.getAccountStatus().getVerificationCode());
		
		EmailRequest emailReq = EmailRequest.builder()
				.to(savedUser.getEmail())
				.title("Account Creation Confirmation")
				.subject("Account Created Successfully")
				.message(message)
				.build();
		
		emailSender.sendEmail(emailReq);
	}

	private void setRole(UserDto userdto, User user) {
		
		List<Integer> requestRoleId = userdto.getRole().stream().map(r -> r.getId()).toList();
		
		List<Role> allById = roleRepository.findAllById(requestRoleId);
		
		user.setRole(allById);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		if(authenticate.isAuthenticated())
		{
			CustomUserDetails c = (CustomUserDetails) authenticate.getPrincipal();
			
			String token = jwtServiceImpl.generateToken(c.getUser());
			
			LoginResponse loginResponse = LoginResponse.builder()
					.token(token)
					.userDto(modelMapper.map(c.getUser(), UserDto.class))
					.build();
			return loginResponse;
		}
		return null;
	}

}
