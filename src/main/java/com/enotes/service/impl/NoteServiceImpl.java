package com.enotes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.enotes.dto.FileDetails;
import com.enotes.dto.NotesDto;
import com.enotes.entity.Notes;
import com.enotes.exceptions.ResourceNotFoundException;
import com.enotes.repository.CategoryRepository;
import com.enotes.repository.FilesRepository;
import com.enotes.repository.NotesRepository;
import com.enotes.service.NotesService;
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
	
	@Value("${file.upload.path}")
	private String uploadPath;
	
	@Override
	public Boolean saveNotes(String notes, MultipartFile multipartFile) throws Exception {
	    ObjectMapper ob = new ObjectMapper();
	    NotesDto notesDto = ob.readValue(notes, NotesDto.class);

	    // Check if category exists
	    checkCategoryExist(notesDto.getCategory().getId());

	    Notes notes1 = modelMapper.map(notesDto, Notes.class);

	    FileDetails fileDetails = saveFileDetails(multipartFile);

	    if (fileDetails != null) {
	        notes1.setFileDetails(fileDetails);  // ✅ Ensuring fileDetails is linked to Notes
	    }

	    Notes savedNotes = notesRepository.save(notes1);

	    return savedNotes != null;
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

}
