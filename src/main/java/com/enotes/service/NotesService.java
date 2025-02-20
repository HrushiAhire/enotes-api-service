package com.enotes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FileDetails;
import com.enotes.dto.NotesDto;
import com.enotes.exceptions.ResourceNotFoundException;

public interface NotesService 
{
	public Boolean saveNotes(String Notes, MultipartFile multipartFile) throws Exception;

	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;
	
}
