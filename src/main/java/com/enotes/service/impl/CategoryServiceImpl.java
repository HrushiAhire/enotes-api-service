package com.enotes.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {
//		Category category = new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIs_active(categoryDto.getIs_active());
		
		Category category = mapper.map(categoryDto, Category.class);
		category.setIs_deleted(false);
		category.setCreated_by(1);
		category.setCreated_on(new Date());
		Category savedCategory = categoryRepository.save(category);
		if(ObjectUtils.isEmpty(savedCategory))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		
		List<Category> categories = categoryRepository.findAll();
		
		List<CategoryDto> categoriesDto = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		
		return categoriesDto;
	}
	
	@Override
	public List<CategoryResponse> getActiveCategories() {
		
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		
		List<CategoryResponse> categoriesDto = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).collect(Collectors.toList());
		
		return categoriesDto;
	}

}
