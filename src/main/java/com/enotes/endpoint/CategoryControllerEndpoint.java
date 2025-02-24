package com.enotes.endpoint;

import static com.enotes.util.Constants.ROLE_ADMIN;
import static com.enotes.util.Constants.ROLE_ADMIN_USER;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enotes.dto.CategoryDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category", description = "All the Category Operation APIs")
@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoint 
{
	@Operation(summary = "Save category", tags = {"Category"}, description = "Admin save category")
	@PostMapping("/save")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);
	
	@Operation(summary = "Get All categories", tags = {"Category"}, description = "Admin Get All categories")
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllCategories();
	
	@Operation(summary = "Get Active Category", tags = {"Category"}, description = "Admin,User Get Active categories")
	@GetMapping("/active")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> getActiveCategories();
	
	@Operation(summary = "Get Category By id", tags = {"Category"}, description = "Admin Get category details")
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Delete Category", tags = {"Category"}, description = "Admin Delete Category")
	@DeleteMapping("/{id}")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
}
