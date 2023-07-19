package ru.parma.filesdistr.repos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Repository
public class FileSystemRepository {

    @Value("${files.baseDir}")
    private String resourcesDir;

    public String save (byte[] content, String fileName) throws IOException {
        Files.createDirectories(Paths.get(resourcesDir));
        Path newFile = Paths.get(resourcesDir +  fileName);
        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }

    public void delete (String location) {
        try {
            java.io.File file = new java.io.File(location);
            if(!file.exists()){
                // TODO logs - no file
            }
            file.deleteOnExit();
        } catch (NullPointerException | SecurityException exception) {
            exception.getStackTrace();
        }
    }


    public FileSystemResource findInFileSystem (String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
