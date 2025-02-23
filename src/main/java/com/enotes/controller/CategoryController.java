package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.endpoint.CategoryControllerEndpoint;
import com.enotes.service.CategoryService;
import com.enotes.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class CategoryController implements CategoryControllerEndpoint
{
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/save")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> saveCategory(CategoryDto categoryDto)
	{
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		
		if(saveCategory)
		{
			return CommonUtil.createBuildResponseMessage("Saved Successfully", HttpStatus.CREATED);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Category Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@GetMapping("/")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllCategories()
	{
		List<CategoryDto> allCategories = categoryService.getAllCategories();
		
		if(CollectionUtils.isEmpty(allCategories))
		{
			return ResponseEntity.noContent().build();
		}
		else
		{
			return CommonUtil.createBuildResponse(allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/active")
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<?> getActiveCategories()
	{
		List<CategoryResponse> allCategories = categoryService.getActiveCategories();
		
		if(CollectionUtils.isEmpty(allCategories))
		{
			return ResponseEntity.noContent().build();
		}
		else
		{
			return CommonUtil.createBuildResponse(allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getCategoryById(Integer id) throws Exception
	{
		CategoryDto categoryById = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryById))
		{
			return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
		}
		else
		{
			return CommonUtil.createBuildResponse(categoryById, HttpStatus.OK);
		}
		
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteCategoryById(Integer id)
	{
		Boolean deleted = categoryService.deleteCategory(id);
		if(deleted)
		{
			return CommonUtil.createBuildResponse("Category deleted successfully", HttpStatus.OK);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
