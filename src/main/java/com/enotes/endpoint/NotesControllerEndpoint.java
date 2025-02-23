package com.enotes.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import static com.enotes.util.Constants.ROLE_ADMIN;
import static com.enotes.util.Constants.ROLE_USER;
import static com.enotes.util.Constants.DEFAULT_PAGE_NO;
import static com.enotes.util.Constants.DEFAULT_PAGE_SIZE;

@RequestMapping("/api/v1/notes")
public interface NotesControllerEndpoint 
{
	@PostMapping("/save")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> saveNotes(@RequestParam String notes, 
			@RequestParam(required=false) MultipartFile multipartFile) throws Exception;
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
			);
	
	@GetMapping("/search-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getNotesByUserSearch(
			@RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(defaultValue = "")String keyword
			);
	
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@DeleteMapping("/deleteAll")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> emptyRecycleBean() throws Exception;
	
	@GetMapping("/fav/{noteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer noteId) throws Exception;
	
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
	@DeleteMapping("/unfav/{favNoteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws Exception;
	
	@GetMapping("/fav-note")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserFavouriteNotes() throws Exception;
}
