package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.repos.FileRepository;
import ru.parma.filesdistr.repos.FileSystemRepository;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileLocationService {
    final int ONE_MB_SIZE_IN_BYTES = 1048576;
    final FileSystemRepository fileSystemRepository;
    final FileRepository fileDbRepository;

    public void save(byte[] bytes, String fileName) throws Exception {
        String location = fileSystemRepository.save(bytes, fileName);// сохраняет на сервер

        ru.parma.filesdistr.models.File f = new File();//parma.File
        f.setName(fileName);
        f.setSize(convertByteToMb(bytes));
        f.setType(getFileType(bytes));
        f.setDateCreated(getDateWithoutTime());
        f.setLocation(location);

        fileDbRepository.save(f);// сохраняет в БД
    }
    public void delete(Long fileId) {
        File fileDb = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String location = fileDb.getLocation();
        fileDbRepository.delete(fileDb);//удаляет в бд
        fileSystemRepository.delete(location);//удаляет с сервера
    }



    Date getDateWithoutTime() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(new Date()));
    }

    Double convertByteToMb(byte @NotNull [] bytes){
        return ((double)bytes.length/(double) ONE_MB_SIZE_IN_BYTES);
    }

    public FileSystemResource get(Long fileId) {
        File file = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }
    String getFileType(byte[] bytes) throws Exception{
        InputStream is = new ByteArrayInputStream(bytes);
        return  URLConnection.guessContentTypeFromStream(is);
    }

}
