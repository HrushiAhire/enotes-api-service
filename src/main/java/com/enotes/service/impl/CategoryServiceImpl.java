package com.enotes.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public Boolean saveCategory(Category category) {
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
	public List<Category> getAllCategories() {
		
		List<Category> allCategories = categoryRepository.findAll();
		
		return allCategories;
	}

}
