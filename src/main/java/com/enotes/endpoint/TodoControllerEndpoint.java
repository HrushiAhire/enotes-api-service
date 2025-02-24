package com.enotes.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static com.enotes.util.Constants.ROLE_USER;

import com.enotes.dto.TodoDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Todo", description = "All the Todo operation APIs")
@RequestMapping("/api/v1/todo")
public interface TodoControllerEndpoint 
{
	@Operation(summary = "Save Todo", tags = {"Todo"}, description = "User Save Todo")
	@PostMapping("/")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveTodo(@RequestBody TodoDto todoDto) throws Exception;
	
	@Operation(summary = "Get Todo by Id", tags = {"Todo"}, description = "User Get Todo by Id")
	@GetMapping("/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get All Todo", tags = {"Todo"}, description = "User get All Todos")
	@GetMapping("/list")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllTodoByUser() throws Exception;
}
