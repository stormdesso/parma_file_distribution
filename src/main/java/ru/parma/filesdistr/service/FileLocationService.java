package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.SavedFileMapper;
import ru.parma.filesdistr.models.File;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.*;
import ru.parma.filesdistr.utils.IPathName;
import ru.parma.filesdistr.utils.Utils;

import java.nio.file.FileSystemNotFoundException;
import java.util.Date;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FileLocationService {
    final FileSystemRepository fileSystemRepository;
    final FileRepository fileDbRepository;
    final ScopeRepository scopeRepository;
    final FolderRepository folderRepository;
    final VersionRepository versionRepository;
    final UserRepository userRepository;


    @Transactional
    public SavedFileDto save ( byte[] bytes, String fileName, String filetype,
                               int generalId, TypeInScopePage typeInScopePage,
                               MediaTypeInScopePage mediaTypeInScopePage
    ) throws Exception {
        Date currDate = Utils.getDateWithoutTime();
        String location = null;

        try {
            if( typeInScopePage == TypeInScopePage.SCOPE ){
                String fullpath = scopeRepository.getReferenceById((long) generalId).getRootPath();
                location = fileSystemRepository.saveInScope(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.FOLDER ){
                String fullpath = folderRepository.getReferenceById((long) generalId).getRootPath();
                location = fileSystemRepository.saveInFolder(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.VERSION ){
                String fullpath = versionRepository.getReferenceById((long) generalId).getRootPath();
                location = fileSystemRepository.saveInVersion(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else throw new IllegalArgumentException();

            File file = File
                    .builder()
                    .name(fileName)
                    .size(Utils.convertByteToMb(bytes))
                    .type(filetype)
                    .dateCreated(currDate)
                    .location(location)
                    .build();

            File savedFile = fileDbRepository.save(file);

            attachFileToEntity(  generalId,  typeInScopePage, mediaTypeInScopePage,  savedFile);

            return SavedFileMapper.INSTANCE.toSaveFileDto(savedFile);

        } catch (Exception e) {
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

    private void attachFileToEntity( int generalId, TypeInScopePage typeInScopePage,
                                     MediaTypeInScopePage mediaTypeInScopePage, File savedFile){
        IPathName iPathName = null;

        if( typeInScopePage == TypeInScopePage.SCOPE ){

            iPathName = scopeRepository.getReferenceById((long) generalId);

            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
                ((Scope)iPathName).getImages().add(savedFile);
            }
            else if(mediaTypeInScopePage == MediaTypeInScopePage.ICON) {
                ((Scope)iPathName).setIcon(savedFile);
            }
            else if( mediaTypeInScopePage == MediaTypeInScopePage.DISTRIBUTION_AGREEMENT){
                ((Scope)iPathName).setDistributionAgreement(savedFile);
            }
            else throw new IllegalArgumentException();

            scopeRepository.save((Scope)iPathName);

        }
        else if( typeInScopePage == TypeInScopePage.FOLDER ){

            iPathName = folderRepository.getReferenceById((long) generalId);

            if(mediaTypeInScopePage == MediaTypeInScopePage.MANIFEST) {
                ((Folder)iPathName).setManifestForIOSFile(savedFile);
            }
            else throw new IllegalArgumentException();

            folderRepository.save((Folder)iPathName);
        }
        else if( typeInScopePage == TypeInScopePage.VERSION ){
            iPathName = versionRepository.getReferenceById((long) generalId);

            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
                ((Version)iPathName).getImages().add(savedFile);
            }
            else if(mediaTypeInScopePage == MediaTypeInScopePage.FILE) {
                ((Version)iPathName).getFiles().add(savedFile);
            }
            else throw new IllegalArgumentException();

            versionRepository.save((Version) iPathName);
        }



    }



    //TODO: bug: не удаляет manifest для folder
    @Transactional
    public void delete ( Long fileId ) {
        Optional<File> optionalFile = fileDbRepository.findById(Math.toIntExact(fileId));
        if(optionalFile.isPresent()){
            File fileDb = optionalFile.get();
            fileDbRepository.delete(fileDb);
        }
        else throw new FileSystemNotFoundException("Файл не найден в БД");


//        if(fileDbRepository.existsById(Math.toIntExact(fileId))) {
//            File fileDb = fileDbRepository.getReferenceById(Math.toIntExact(fileId));
//            String location = fileDb.getLocation();
//
//            fileDbRepository.deleteById(Math.toIntExact(fileId));//удаляет в бд
//            fileSystemRepository.delete(location);//удаляет с сервера
//        }
//        else throw new FileSystemNotFoundException("Файл не найден в БД");
    }

    public FileSystemResource get ( Long fileId ) {
        File file = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }

}
