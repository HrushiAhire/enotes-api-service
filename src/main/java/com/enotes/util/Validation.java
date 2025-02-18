package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.exceptions.ValidationException;

@Component
public class Validation {
	
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
				if(categoryDto.getName().length() < 10)
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
}
