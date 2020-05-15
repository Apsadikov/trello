package ru.itis.trello.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileServiceImpl implements FileService {
    private Environment environment;
    private BCryptPasswordEncoder bCrypt;

    @Autowired
    public FileServiceImpl(BCryptPasswordEncoder bCrypt, Environment environment) {
        this.bCrypt = bCrypt;
        this.environment = environment;
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String extension = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
        String name = bCrypt.encode(file.getOriginalFilename()).replaceAll("/\\W/", "");
        File newFile = new File(environment.getProperty("storage.path") + name + extension);
        IOUtils.copyLarge(file.getInputStream(), new FileOutputStream(newFile));
        return newFile.getName();
    }

    @Override
    public File loadFile(String fileName) throws FileNotFoundException {
        File file = new File(String.valueOf(environment.getProperty("storage.path") + fileName));
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
