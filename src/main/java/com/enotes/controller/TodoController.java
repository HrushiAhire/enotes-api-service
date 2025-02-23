package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.dto.TodoDto;
import com.enotes.endpoint.TodoControllerEndpoint;
import com.enotes.service.TodoService;
import com.enotes.util.CommonUtil;

@RestController
public class TodoController implements TodoControllerEndpoint
{
	
	@Autowired
	private TodoService todoService;
	
	public ResponseEntity<?> saveTodo(TodoDto todoDto) throws Exception
	{
		Boolean saveTodo = todoService.saveTodo(todoDto);
		
		if(saveTodo)
		{
			return CommonUtil.createBuildResponseMessage("Todo saved Successfully", HttpStatus.CREATED);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Todo not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getTodoById(Integer id) throws Exception
	{
		TodoDto todoById = todoService.getTodoById(id);
		
		return CommonUtil.createBuildResponse(todoById, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity<?> getAllTodoByUser() throws Exception
	{
		List<TodoDto> todoByUser = todoService.getTodoByUser();
		
		if(CollectionUtils.isEmpty(todoByUser))
		{
			return ResponseEntity.noContent().build();
		}
		
		return CommonUtil.createBuildResponse(todoByUser, HttpStatus.OK);
	}
	
	
}
