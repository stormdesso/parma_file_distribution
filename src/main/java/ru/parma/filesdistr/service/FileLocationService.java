package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.repos.FileRepository;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.utils.Utils;

@Service
@RequiredArgsConstructor
public class FileLocationService {
    final FileSystemRepository fileSystemRepository;
    final FileRepository fileDbRepository;

    public FileDto save(byte[] bytes, String fileName, String filetype) throws Exception {
        String location = fileSystemRepository.save(bytes, fileName);// сохраняет на сервер

        File file = File
                .builder()
                    .name(fileName)
                    .size(Utils.convertByteToMb(bytes))
                    .type(filetype)
                    .dateCreated(Utils.getDateWithoutTime())
                    .location(location)
                .build();

        fileDbRepository.save(file);// сохраняет в БД
        //TODO: добавить маппер (JMapper, MapStruct не сработали)
        return entityToDto(file);
    }
    private FileDto entityToDto(@NotNull File file){
        return new FileDto(){{
            setId(file.getId());
            setName(file.getName());
            setSize(file.getSize());
            setType(file.getType());
            setDateCreated(file.getDateCreated());
            setLocation(file.getLocation());
        }};
    }

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
