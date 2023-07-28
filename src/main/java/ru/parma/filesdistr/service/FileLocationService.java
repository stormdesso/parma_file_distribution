package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.mappers.FileMapper;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.repos.FileRepository;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.utils.Utils;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class FileLocationService {

    final FileSystemRepository fileSystemRepository;
    final FileRepository fileDbRepository;

    @Transactional
    public SavedFileDto save(byte[] bytes, String fileName, String filetype) throws Exception {
        Date currDate = Utils.getDateWithoutTime();
        String location = null;
        try {

            location = fileSystemRepository.save(bytes, fileName);// сохраняет на сервер

            File file = File
                    .builder()
                    .name(fileName)
                    .size(Utils.convertByteToMb(bytes))
                    .type(filetype)
                    .dateCreated(currDate)
                    .location(location)
                    .build();

            // сохраняет в БД
            File savedFile = fileDbRepository.save(file);
            SavedFileDto savedFileDto = FileMapper.INSTANCE.toSaveFileDto(savedFile);
            return savedFileDto;

        } catch (Exception e) {
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

    @Transactional
    public void delete(Long fileId) {
        File fileDb = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String location = fileDb.getLocation();
        fileDbRepository.delete(fileDb);//удаляет в бд
        fileSystemRepository.delete(location);//удаляет с сервера
    }


    public FileSystemResource get(Long fileId) {
        File file = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }

}
