package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNoteDto;
import com.enotes.dto.FileDetails;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.service.NotesService;
import com.enotes.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController 
{
	@Autowired
	private NotesService notesService;
	
	@PostMapping("/save")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, 
			@RequestParam(required=false) MultipartFile multipartFile) throws Exception
	{
		Boolean savedNote = notesService.saveNotes(notes, multipartFile);
		if(savedNote)
		{
			return CommonUtil.createBuildResponseMessage("Note created", HttpStatus.CREATED);
		}
		else 
		{
			return CommonUtil.createErrorResponseMessage("Note not saved", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception
	{
		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);
		
		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
		return ResponseEntity.ok().headers(headers).body(data);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllNotes()
	{
		List<NotesDto> allNotes = notesService.getAllNotes();
		
		if(CollectionUtils.isEmpty(allNotes))
		{
			return ResponseEntity.noContent().build();
		}
		else
		{
			return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
		}
	}
	
	@GetMapping("/user-notes")
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize
			)
	{
		Integer userId = 1;
		NotesResponse allNotes = notesService.getAllNotesByUser(userId, pageNo, pageSize);
		
		
		
//		if(CollectionUtils.isEmpty(allNotes))
//		{
//			return ResponseEntity.noContent().build();
//		}
//		else
//		{
			return CommonUtil.createBuildResponse(allNotes, HttpStatus.OK);
//		}
	}
	
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception
	{
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);
	}

	@GetMapping("/restore/{id}")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception
	{
		notesService.restoreNotes(id);
		return CommonUtil.createBuildResponseMessage("Restore success", HttpStatus.OK);
	}
	
	@GetMapping("/recycle-bin")
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception
	{
		Integer userId = 1;
		
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		
		if(CollectionUtils.isEmpty(notes))
		{
			return CommonUtil.createBuildResponseMessage("Notes not available in Recycle Bin", HttpStatus.OK);			
		}
		return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception
	{
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteAll")
	public ResponseEntity<?> emptyRecycleBean() throws Exception
	{
		int userId = 1;
		notesService.emptyRecycleBean(userId);
		return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);
	}
	
	@GetMapping("/fav/{noteId}")
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer noteId) throws Exception
	{
		int userId = 1;
		notesService.favouriteNotes(noteId);
		return CommonUtil.createBuildResponseMessage("Notes added to favourite", HttpStatus.CREATED);
	}
	
	@GetMapping("/copy/{id}")
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception
	{
		int userId = 1;
		Boolean copyNotes = notesService.copyNotes(id);
		if(copyNotes)
		{
			return CommonUtil.createBuildResponseMessage("Copied Successfully", HttpStatus.CREATED);
		}
		else
		{
			return CommonUtil.createErrorResponseMessage("Could not copy notes", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/unfav/{favNoteId}")
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws Exception
	{
		int userId = 1;
		notesService.unFavouriteNotes(favNoteId);
		return CommonUtil.createBuildResponseMessage("Note removed from favourite", HttpStatus.OK);
	}
	
	@GetMapping("/fav-note")
	public ResponseEntity<?> getUserFavouriteNotes() throws Exception
	{
		int userId = 1;
		List<FavouriteNoteDto> favouriteNotes = notesService.getUserFavouriteNotes();
		if(!CollectionUtils.isEmpty(favouriteNotes))
		{
			return CommonUtil.createBuildResponse(favouriteNotes, HttpStatus.OK);
		}
		else
		{
			return ResponseEntity.noContent().build();
		}
	}
}
