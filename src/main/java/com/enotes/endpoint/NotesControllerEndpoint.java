package com.enotes.endpoint;

import static com.enotes.util.Constants.DEFAULT_PAGE_NO;
import static com.enotes.util.Constants.DEFAULT_PAGE_SIZE;
import static com.enotes.util.Constants.ROLE_ADMIN;
import static com.enotes.util.Constants.ROLE_ADMIN_USER;
import static com.enotes.util.Constants.ROLE_USER;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.NotesRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Notes", description = "All the Notes operation APIs")
@RequestMapping("/api/v1/notes")
public interface NotesControllerEndpoint 
{
	@Operation(summary = "Save Notes", tags = {"Notes","User"}, description = "User Save notes")
	@PostMapping(value =  "/save", consumes = "multipart/form-data")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> saveNotes(
			@RequestParam 
			@Parameter(description = "Json String notes", required = true, content = @Content(schema = @Schema(implementation = NotesRequest.class))) String notes, 
			@RequestParam(required=false) MultipartFile multipartFile) throws Exception;
	
	@Operation(summary = "Download Uploaded file", tags = {"Notes","User"}, description = "Download file")
	@GetMapping("/download/{id}")
	@PreAuthorize(ROLE_ADMIN_USER)
	public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get All Notes", tags = {"Notes"}, description = "Admin Get All Notes")
	@GetMapping("/")
	@PreAuthorize(ROLE_ADMIN)
	public ResponseEntity<?> getAllNotes();
	
	@Operation(summary = "Get all notes of a User", tags = {"Notes","User"}, description = "User Get all notes of a user")
	@GetMapping("/user-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getAllNotesByUser(
			@RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize
			);
	
	@Operation(summary = "Search notes by Keyword", tags = {"Notes","User"}, description = "User search notes by keyword")
	@GetMapping("/search-notes")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getNotesByUserSearch(
			@RequestParam(defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize,
			@RequestParam(defaultValue = "")String keyword
			);
	
	@Operation(summary = "Delete Notes", tags = {"Notes","User"}, description = "User delete notes")
	@GetMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Restore notes from deleted notes", tags = {"Notes","User"}, description = "User Restore Notes")
	@GetMapping("/restore/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get Notes from Recycle Bin", tags = {"Notes","User"}, description = "User Get Recycle Bin Notes")
	@GetMapping("/recycle-bin")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;
	
	@Operation(summary = "Delete notes permanently", tags = {"Notes","User"}, description = "User delete notes permanently")
	@DeleteMapping("/delete/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Empty recycle bean", tags = {"Notes","User"}, description = "User Delete all notes from recycle bin")
	@DeleteMapping("/deleteAll")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> emptyRecycleBean() throws Exception;
	
	@Operation(summary = "Add notes to favourites", tags = {"Notes","User"}, description = "User Add notes to favourites")
	@GetMapping("/fav/{noteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer noteId) throws Exception;
	
	@Operation(summary = "Copy Notes", tags = {"Notes","User"}, description = "User copy notes")
	@GetMapping("/copy/{id}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Removes notes from favourites", tags = {"Notes","User"}, description = "User Remove note from favourite")
	@DeleteMapping("/unfav/{favNoteId}")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favNoteId) throws Exception;
	
	@Operation(summary = "Get user favourite notes", tags = {"Notes","User"}, description = "User Get user favourite notes")
	@GetMapping("/fav-note")
	@PreAuthorize(ROLE_USER)
	public ResponseEntity<?> getUserFavouriteNotes() throws Exception;
}
