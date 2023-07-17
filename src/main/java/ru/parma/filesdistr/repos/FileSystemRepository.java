package ru.parma.filesdistr.repos;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@Repository
public
class FileSystemRepository {

    //TODO: придумать, где хранить файлы + exception
    final String RESOURCES_DIR = "files\\";

    public String save(byte[] content, String fileName) throws Exception {

        Path newFile = Paths.get(RESOURCES_DIR + new Date().getTime() + "-" + fileName);
        Files.createDirectories(newFile.getParent());
        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }

    public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            // Handle access or file not found problems.
            throw new RuntimeException();
        }
    }
}
