package com.enotes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FavouriteNoteDto;
import com.enotes.dto.FileDetails;
import com.enotes.dto.NotesDto;
import com.enotes.dto.NotesDto.FilesDto;
import com.enotes.dto.NotesResponse;
import com.enotes.entity.FavouriteNote;
import com.enotes.entity.Notes;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.FavouriteNotesRepository;
import com.enotes.repository.FilesRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;
import com.enotes.util.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NoteServiceImpl implements NotesService{
	
	@Autowired
	private NotesRepository notesRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FilesRepository fileRepo;
	
	@Autowired
	private FavouriteNotesRepository favouriteNotesRepository;
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Override
	public Boolean saveNotes(String notes, MultipartFile multipartFile) throws Exception {
	    ObjectMapper ob = new ObjectMapper();
	    NotesDto notesDto = ob.readValue(notes, NotesDto.class);
	   notesDto.setDeleted(false);
	   notesDto.setDeletedOn(null);

	    if(!ObjectUtils.isEmpty(notesDto.getId()))
	    {
	    	updateNotes(notesDto, multipartFile);
	    }
	    
	    // Check if category exists
	    checkCategoryExist(notesDto.getCategory().getId());

	    Notes notes1 = modelMapper.map(notesDto, Notes.class);

	    FileDetails fileDetails = saveFileDetails(multipartFile);

	    if (!ObjectUtils.isEmpty(fileDetails)) {
	        notes1.setFileDetails(fileDetails);  // ✅ Ensuring fileDetails is linked to Notes
	    }else {
	    	if(ObjectUtils.isEmpty(notesDto.getId()))
		    {
		    	notes1.setFileDetails(null);
		    }
	    }

	    Notes savedNotes = notesRepository.save(notes1);

	    if(!ObjectUtils.isEmpty(savedNotes))
	    {
	    	return true;
	    }
	    return false;
	}


	@Override
	public void softDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Notes id not found"));
		
		notes.setDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Notes id not found"));
		
		notes.setDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);
		
	}


	@Override
	public List<NotesDto> getUserRecycleBinNotes() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		
		List<NotesDto> list = recycleNotes.stream().map(note -> modelMapper.map(note, NotesDto.class)).toList();
		
		return list;
	}


	private void updateNotes(NotesDto notesDto, MultipartFile multipartFile) throws Exception {
		Notes existNotes = notesRepository.findById(notesDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));
		
		if(ObjectUtils.isEmpty(multipartFile))
		{
			notesDto.setFileDetails(modelMapper.map(existNotes.getFileDetails(), FilesDto.class));
		}
	}


	private FileDetails saveFileDetails(MultipartFile multipartFile) throws IOException {
		
		if(!ObjectUtils.isEmpty(multipartFile) && !multipartFile.isEmpty())
		{
			String originalFilename = multipartFile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);
			
			List<String> allowedExtensions = Arrays.asList("pdf","xlsx","jpg","png");
			
			if(!allowedExtensions.contains(extension))
			{
				throw new IllegalArgumentException("Invalid file format! Please uplaod correct file format");
			}			
			
			String randomString = UUID.randomUUID().toString();
			
			String uploadFileName = randomString+ "." +extension;
			
			File saveFile = new File(uploadPath);
			if(!saveFile.exists())
			{
				saveFile.mkdir();
			}
			
			String storePath = Paths.get(uploadPath, uploadFileName).toString();
			
			//upload file
			long upload = Files.copy(multipartFile.getInputStream(), Paths.get(storePath));
			if(upload != 0)
			{
				FileDetails fileDetails = new FileDetails();

				fileDetails.setOriginalFileName(originalFilename);
				fileDetails.setDisplayFileName(getDisplayName(originalFilename));
				fileDetails.setUploadFileName(uploadFileName); 
				fileDetails.setFileSize(multipartFile.getSize());
				fileDetails.setPath(storePath);
				FileDetails save = fileRepo.save(fileDetails);
				return save;
			}
		}
		
		
		return null;
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {		
		InputStream io = new FileInputStream(fileDetails.getPath());
		
		byte[] byteData = StreamUtils.copyToByteArray(io);
		
		return byteData;
	}


	@Override
	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {
		Pageable pageable =  PageRequest.of(pageNo, pageSize);
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Page<Notes> notes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);
		
		List<NotesDto> notesList = notes.get().map((note) -> modelMapper.map(note, NotesDto.class)).toList();
		
		NotesResponse notesResponse = NotesResponse.builder()
				.notes(notesList)
				.pageNumber(notes.getNumber())
				.pageSize(notes.getSize())
				.totalElements(notes.getTotalElements())
				.totalPages(notes.getTotalPages())
				.isFirst(notes.isFirst())
				.isLast(notes.isLast())
				.build();
		return notesResponse;
	}


	@Override
	public FileDetails getFileDetails(Integer id) throws Exception {
		FileDetails fileDetails = fileRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("File is not available"));
		
		return fileDetails;
	}


	private String getDisplayName(String originalFilename) {
		
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);
		
		if(fileName.length() > 8)
		{
			fileName = fileName.substring(0, 7);
		}
		fileName = fileName+"."+extension;
		return fileName;
	}

	private void checkCategoryExist(Integer id) throws Exception {
		
		categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));
	}

	@Override
	public List<NotesDto> getAllNotes() 
	{
		List<Notes> allNotes = notesRepository.findAll();
		
		List<NotesDto> list = allNotes.stream().map(notes -> modelMapper.map(notes, NotesDto.class)).toList();
		
		return list;
	}


	@Override
	public void hardDeleteNotes(Integer id) throws Exception {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Notes id not found"));
		
		if(notes.isDeleted())
		{
			notesRepository.delete(notes);
		}
		else
		{
			throw new IllegalArgumentException("We can not note directly");
		}
	}


	@Override
	public void emptyRecycleBean() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleBinNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		
		if(!CollectionUtils.isEmpty(recycleBinNotes))
		{
			notesRepository.deleteAll();
		}
	}


	@Override
	public void favouriteNotes(Integer noteId) throws Exception {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Notes note = notesRepository.findById(noteId).orElseThrow(() -> new ResourceNotFoundException("Note does not exist"));
		
		FavouriteNote favNote = FavouriteNote.builder()
				.userId(userId)
				.note(note)
				.build();
		
		favouriteNotesRepository.save(favNote);
	}


	@Override
	public void unFavouriteNotes(Integer favNoteId) throws Exception {
		FavouriteNote favNote = favouriteNotesRepository.findById(favNoteId).orElseThrow(() -> new ResourceNotFoundException("Favourite Note does not exist"));
		
		favouriteNotesRepository.delete(favNote);
	}


	@Override
	public List<FavouriteNoteDto> getUserFavouriteNotes() throws Exception {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		
		List<FavouriteNote> notes =  favouriteNotesRepository.findByUserId(userId);
		
		List<FavouriteNoteDto> notesDtoList = notes.stream().map((note) -> modelMapper.map(note, FavouriteNoteDto.class)).toList();
		
		return notesDtoList;
	}


	@Override
	public Boolean copyNotes(Integer id) throws Exception {
		
		Notes note = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note does not exist"));
		
		Notes copyNote = Notes.builder()
				.title(note.getTitle())
				.description(note.getDescription())
				.category(note.getCategory())
				.deletedOn(null)
				.isDeleted(false)
				.fileDetails(null)
				.build();
		
		Notes saveCopyNote = notesRepository.save(copyNote);
		
		if(!ObjectUtils.isEmpty(saveCopyNote))
		{
			return true;
		}
		return false;
	}	
}
