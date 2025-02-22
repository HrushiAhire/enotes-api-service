package com.enotes.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.TodoDto;
import com.enotes.dto.UserDto;
import com.enotes.dto.UserDto.RoleDto;
import com.enotes.enums.TodoStatusConstants;
import com.enotes.exceptions.ExistDataException;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.exceptions.ValidationException;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;

@Component
public class Validation {
		
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public void categoryValidation(CategoryDto categoryDto)
	{
		Map<String, Object> error = new LinkedHashMap<>();
		
		if(ObjectUtils.isEmpty(categoryDto))
		{
			throw new IllegalArgumentException("Category object should not be null");
		}
		else
		{
			//Validation name field
			if(ObjectUtils.isEmpty(categoryDto.getName()))
			{
				error.put("name", "name field is empty or null");
			}
			else
			{
				if(categoryDto.getName().length() < 2)
				{
					error.put("name", "Name length min 10");
				}
				if(categoryDto.getName().length() > 100)
				{
					error.put("name", "Name length max 100");
				}
			}
			
			//validation description
			if(ObjectUtils.isEmpty(categoryDto.getName()))
			{
				error.put("description", "description field is empty or null");
			}
			
			if(ObjectUtils.isEmpty(categoryDto.getIsActive()))
			{
				error.put("isActive", "isActive field is empty or null");
			}
			else
			{
				if(categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() != Boolean.FALSE.booleanValue())
				{
					error.put("isActive", "Invalid valur of isActive field");
				}
			}
		}
		
		if(!error.isEmpty())
		{
			throw new ValidationException(error);
		}
	}
	
	public void todoValidation(TodoDto todoDto) throws Exception {
	    TodoDto.StatusDto reqStatus = todoDto.getStatus();
	    
	    // Check if the provided status ID exists in the STATUS_MAP
	    if (!TodoStatusConstants.STATUS_MAP.containsKey(reqStatus.getId())) {
	        throw new ResourceNotFoundException("Invalid status");
	    }
	}
	
	public void roleValidation(UserDto userDto)
	{
		if(!StringUtils.hasText(userDto.getFirstName()))
		{
			throw new IllegalArgumentException("First name is invalid");
		}
		
		if(!StringUtils.hasText(userDto.getLastName()))
		{
			throw new IllegalArgumentException("last name is invalid");
		}
		
		if(!StringUtils.hasText(userDto.getEmail()) || !userDto.getEmail().matches(Constants.EMAIL_REGEX))
		{
			throw new IllegalArgumentException("email name is invalid");
		}
		else
		{
			//validate exisitng email id
			Boolean existsEmail = userRepository.existsByEmail(userDto.getEmail());
			if(existsEmail)
			{
				throw new ExistDataException("Email already exists");
			}
		}
		
		if(!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.MOBILE))
		{
			throw new IllegalArgumentException("Mobile number is invalid");
		}
		
		if(CollectionUtils.isEmpty(userDto.getRole()))
		{
			throw new IllegalArgumentException("Role is Invalid! Please use valid role");
		}
		else
		{
			List<Integer> roleIdList = roleRepository.findAll().stream().map(r -> r.getId()).toList();
			
			List<Integer> invalidRoleIds = new ArrayList<>();
			for (RoleDto r : userDto.getRole()) {
			    Integer roleId = r.getId();
			    if (!roleIdList.contains(roleId)) {
			    	invalidRoleIds.add(roleId);
			    }
			}

			if(!CollectionUtils.isEmpty(invalidRoleIds))
			{
				throw new IllegalArgumentException("Role is Invalid" + invalidRoleIds);
			}
		}
		
		
	}
}
