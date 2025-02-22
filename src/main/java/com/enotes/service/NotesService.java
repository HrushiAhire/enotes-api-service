package com.enotes.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNoteDto;
import com.enotes.dto.FileDetails;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;

public interface NotesService 
{
	public Boolean saveNotes(String Notes, MultipartFile multipartFile) throws Exception;

	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id) throws Exception;

	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id) throws Exception;

	public void restoreNotes(Integer id) throws Exception;

	public List<NotesDto> getUserRecycleBinNotes();

	public void hardDeleteNotes(Integer id) throws Exception;

	public void emptyRecycleBean();
	
	
	public List<FavouriteNoteDto> getUserFavouriteNotes() throws Exception;

	void favouriteNotes(Integer noteId) throws Exception;

	void unFavouriteNotes(Integer noteId) throws Exception;

	public Boolean copyNotes(Integer id) throws Exception;
	
}
