package com.jobUploader.service;

import com.jobUploader.exception.FileStorageException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class represents the FileStorage service which does the actual storage of file to the local storage
 * system
 */
@Component
public class FileStorageService {
    private final String uploadLocation;
    private final Path uploadPath;


    public FileStorageService(@Value("${file.upload.dir}")String uploadLocation) {
        this.uploadLocation = uploadLocation;
        this.uploadPath = createAndGetPath(uploadLocation);
    }
    private Path createAndGetPath(String location) {
        Path path = Paths.get(location);
        if (!path.toFile().exists()) {
            path.toFile().mkdirs();
        }
        return path;
    }
    public void store(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new FileStorageException("Empty file name");
        }
        int indexOfLastSlash = -1;
        if(file!=null ) {
            indexOfLastSlash = file.getOriginalFilename()!= null ? file.getOriginalFilename().lastIndexOf("\\") : -1; //NOSONAR
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename()).substring(indexOfLastSlash + 1);
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new FileStorageException("Cannot store file with relative path outside current directory " + filename);
            }
            copyFileContents(filename, file.getInputStream());
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + filename, e);
        }
    }

    protected void copyFileContents(String destinationFilename, InputStream inputStream) throws IOException {
        File file = this.uploadPath.resolve(destinationFilename).toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(convertTabsToSpace(line) + System.lineSeparator());
            }
            writer.flush();
        }
    }

    public static String convertTabsToSpace(String data) {
        return data.replace("\t", " ");
    }


}
