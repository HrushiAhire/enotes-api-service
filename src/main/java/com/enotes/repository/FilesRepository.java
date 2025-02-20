package com.enotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.enotes.dto.FileDetails;

public interface FilesRepository extends JpaRepository<FileDetails, Integer>{

}
