package com.enotes.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.entity.Todo;
import com.enotes.enums.TodoStatusConstants;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.repository.TodoRepository;
import com.enotes.service.TodoService;
import com.enotes.util.Validation;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Validation validation;

    @Override
    public Boolean saveTodo(TodoDto todoDto) throws Exception {
        // Validate Todo status
        validation.todoValidation(todoDto);

        Todo todo = modelMapper.map(todoDto, Todo.class);
        todo.setStatusId(todoDto.getStatus().getId());
        Todo savedTodo = todoRepository.save(todo);

        return !ObjectUtils.isEmpty(savedTodo);
    }

    @Override
    public TodoDto getTodoById(Integer id) throws Exception {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id is invalid"));

        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        setStatus(todoDto, todo);
        return todoDto;
    }

    private void setStatus(TodoDto todoDto, Todo todo) {
        if (TodoStatusConstants.STATUS_MAP.containsKey(todo.getStatusId())) {
            StatusDto statusDto = StatusDto.builder()
                    .id(todo.getStatusId())
                    .name(TodoStatusConstants.STATUS_MAP.get(todo.getStatusId()))
                    .build();
            todoDto.setStatus(statusDto);
        }
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        Integer userId = 1;

        List<Todo> todos = todoRepository.findByCreatedBy(userId);

        return todos.stream()
                .map(todo -> {
                    TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
                    setStatus(todoDto, todo);
                    return todoDto;
                })
                .toList();
    }
}
