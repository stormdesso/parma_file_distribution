package ru.parma.filesdistr.repos;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Repository
public class FileSystemRepository{

    //WARN: файлы перезаписывается, если такое имя уже занято!!!
    @Value("${files.baseDir}")
    private String resourcesDir;

    private @NotNull String createFileLocation( String path, String fileName, byte[] content ) throws IOException {
        java.io.File dir = new java.io.File(path);// создали директорию

        if(!dir.mkdirs()){
            //TODO: exception + log
        }

        path += "//" + fileName;
        Path newFile = Paths.get(path);
        Files.write(newFile, content);
        return newFile.toAbsolutePath().toString();
    }
    public String saveInScope ( byte[] content, String fileName, MediaTypeInScopePage mediaTypeInScopePage,
                                String fullPath) throws IOException {
        Files.createDirectories(Paths.get(resourcesDir));
        String path = resourcesDir + fullPath;

        if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
            path += MediaTypeInScopePage.ILLUSTRATION.toString().toLowerCase();
        }
        if(mediaTypeInScopePage == MediaTypeInScopePage.ICON) {
            path += MediaTypeInScopePage.ICON.toString().toLowerCase();
        }
        if( mediaTypeInScopePage == MediaTypeInScopePage.DISTRIBUTION_AGREEMENT){
            path += MediaTypeInScopePage.DISTRIBUTION_AGREEMENT.toString().toLowerCase();
        }
        else throw new IllegalArgumentException();

        return createFileLocation(path, fileName, content);
    }

    public String saveInFolder( byte[] content, String fileName, MediaTypeInScopePage mediaTypeInScopePage,
                                String fullPath) throws IOException {
        Files.createDirectories(Paths.get(resourcesDir));
        String path = resourcesDir + fullPath + "//";

        if(mediaTypeInScopePage == MediaTypeInScopePage.MANIFEST) {
            path += MediaTypeInScopePage.MANIFEST.toString().toLowerCase();
        }
        else throw new IllegalArgumentException();

        return createFileLocation(path, fileName, content);
    }

    public String saveInVersion( byte[] content, String fileName, MediaTypeInScopePage mediaTypeInScopePage,
                                String fullPath) throws IOException {
        Files.createDirectories(Paths.get(resourcesDir));
        String path = resourcesDir + fullPath + "//";

        if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
            path += MediaTypeInScopePage.ILLUSTRATION.toString().toLowerCase();
        }
        else if(mediaTypeInScopePage == MediaTypeInScopePage.FILE) {
            path += MediaTypeInScopePage.FILE.toString().toLowerCase();
        }
        else throw new IllegalArgumentException();

        return createFileLocation(path, fileName, content);
    }

    public void delete ( String location ) {
        try {
            java.io.File file = new java.io.File(location);
            if(! file.exists()) {
                // TODO logs - no file
            }
            file.deleteOnExit();
        } catch (NullPointerException | SecurityException exception) {
            exception.getStackTrace();
        }
    }


    public FileSystemResource findInFileSystem ( String location ) {
        try {
            return new FileSystemResource(Paths.get(location));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}