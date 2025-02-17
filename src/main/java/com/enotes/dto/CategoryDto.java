package com.enotes.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto 
{
	private Integer id;
	
	private String name;
	
	private String description;
	
	private Boolean isActive;
	
	private Integer created_by;
	
	private Date created_on;
	
	private Integer updated_by;
	
	private Date updated_on;
}
