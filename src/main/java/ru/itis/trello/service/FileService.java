package ru.itis.trello.service;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;

    File loadFile(String fileName) throws FileNotFoundException;
}
