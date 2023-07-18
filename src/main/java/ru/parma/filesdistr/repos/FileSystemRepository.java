package ru.parma.filesdistr.repos;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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
    public void delete(String location) {
        try {
            java.io.File file = new java.io.File(location);
            if (!file.delete()) {//удаляет с сервера
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (NullPointerException | SecurityException exception) {
            exception.getStackTrace();
        }
    }


        public FileSystemResource findInFileSystem(String location) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
