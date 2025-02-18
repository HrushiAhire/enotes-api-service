package com.enotes.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.entity.Category;
import com.enotes.exceptions.ResourceNotFoundException;
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
		
		if(ObjectUtils.isEmpty(category.getId()))
		{
			category.setIsDeleted(false);
			category.setCreated_by(1);
			category.setCreated_on(new Date());
		}
		else
		{
			updateCategory(category);
		}
		
		Category savedCategory = categoryRepository.save(category);
		if(ObjectUtils.isEmpty(savedCategory))
		{
			return false;
		}
		
		return true;
	}

	private void updateCategory(Category category) {
		
		Optional<Category> cat = categoryRepository.findById(category.getId());
		if(cat.isPresent())
		{
			Category existing = cat.get();
			category.setCreated_by(existing.getCreated_by());
			category.setCreated_on(existing.getCreated_on());
			category.setIsDeleted(existing.getIsDeleted());
			
			category.setUpdated_by(1);
			category.setUpdated_on(new Date());
		}
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		
		List<Category> categories = categoryRepository.findByIsDeletedFalse();
		
		List<CategoryDto> categoriesDto = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).collect(Collectors.toList());
		
		return categoriesDto;
	}
	
	@Override
	public List<CategoryResponse> getActiveCategories() {
		
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		
		List<CategoryResponse> categoriesDto = categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).collect(Collectors.toList());
		
		return categoriesDto;
	}
	@Override
	public CategoryDto getCategoryById(Integer id) throws Exception {
		Category category = categoryRepository.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id : "+id));
		
		if(!ObjectUtils.isEmpty(category))
		{
			return mapper.map(category, CategoryDto.class);
		}
		else
		{
			return null;
		}
	}

	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> category = categoryRepository.findById(id);
		
		if(category.isPresent())
		{
			Category category2 = category.get();
			category2.setIsDeleted(true);
			categoryRepository.save(category2);
			return true;
		}
		return false;
	}
}
