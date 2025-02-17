package com.enotes.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseModel 
{
	private Boolean isActive;
	
	public Boolean isDeleted;
	
	private Integer created_by;
	
	private Date created_on;
	
	private Integer updated_by;
	
	private Date updated_on;
}
