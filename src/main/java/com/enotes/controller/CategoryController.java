package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.service.CategoryService;
import com.enotes.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController 
{
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/save")
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto)
	{
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		
		if(saveCategory)
		{
			return CommonUtil.createBuildResponseMessage("Saved Successfully", HttpStatus.CREATED);
//			return new ResponseEntity<>("saved", HttpStatus.CREATED);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Category Not Saved", HttpStatus.INTERNAL_SERVER_ERROR);
//			return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	@GetMapping("/")
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
//			return new ResponseEntity<>(allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/active")
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
//			return new ResponseEntity<>(allCategories, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws Exception
	{
		CategoryDto categoryById = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryById))
		{
			return CommonUtil.createErrorResponseMessage("Internal Server Error", HttpStatus.NOT_FOUND);
//			return new ResponseEntity<>("Internal Server Error", HttpStatus.NOT_FOUND);
		}
		else
		{
			return CommonUtil.createBuildResponse(categoryById, HttpStatus.OK);
//			return new ResponseEntity<>(categoryById, HttpStatus.OK);
		}
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id)
	{
		Boolean deleted = categoryService.deleteCategory(id);
		if(deleted)
		{
			return CommonUtil.createBuildResponse("Category deleted successfully", HttpStatus.OK);
//			return new ResponseEntity<>("Category with id deleted successfully : " + id, HttpStatus.OK);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
//			return new ResponseEntity<>("Category not deleted", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
