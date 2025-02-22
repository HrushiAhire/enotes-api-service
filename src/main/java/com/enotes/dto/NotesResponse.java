package com.enotes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesResponse 
{
	private List<NotesDto> notes;
	
	private Integer pageNumber;
	
	private Integer pageSize;
	
	private Long totalElements;
	
	private Integer totalPages;
	
	private boolean isFirst;
	
	private boolean isLast;
	
}
