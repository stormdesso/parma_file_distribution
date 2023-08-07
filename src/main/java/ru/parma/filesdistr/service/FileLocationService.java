package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.FileMapper;
import ru.parma.filesdistr.models.*;
import ru.parma.filesdistr.repos.*;
import ru.parma.filesdistr.utils.IPathName;
import ru.parma.filesdistr.utils.Utils;

import java.nio.file.FileSystemNotFoundException;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class FileLocationService {
    final FileSystemRepository fileSystemRepository;
    final FileRepository fileDbRepository;
    final ScopeRepository scopeRepository;
    final FolderRepository folderRepository;
    final VersionRepository versionRepository;
    final UserRepository userRepository;
    final TagRepository tagRepository;


    @Transactional
    public FileDto save (byte[] bytes, String fileName, String filetype,
                         Long generalId, TypeInScopePage typeInScopePage,
                         MediaTypeInScopePage mediaTypeInScopePage,
                         @Nullable Long tagId,
                         @Nullable String comment) throws Exception {
        Date currDate = Utils.getDateWithoutTime();
        String location = null;

        try {
            if( typeInScopePage == TypeInScopePage.SCOPE ){
                String fullpath = scopeRepository.getReferenceById(generalId).getRootPath();
                location = fileSystemRepository.saveInScope(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.FOLDER ){
                String fullpath = folderRepository.getReferenceById(generalId).getRootPath();
                location = fileSystemRepository.saveInFolder(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.VERSION ){
                String fullpath = versionRepository.getReferenceById(generalId).getRootPath();
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

            if(mediaTypeInScopePage == MediaTypeInScopePage.FILE & typeInScopePage == TypeInScopePage.VERSION){
                Tag tag = null;
                if(tagId != null) {
                    tag = tagRepository.getReferenceById( tagId );
                    file.setTag( tag );
                }
                if(comment != null){
                    file.setComment(comment);
                }
            }

            File savedFile = fileDbRepository.save(file);

            attachFileToEntity(generalId,  typeInScopePage, mediaTypeInScopePage,  savedFile);
            return FileMapper.INSTANCE.toFileDto(savedFile);

        } catch (Exception e) {
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

    private void attachFileToEntity( long generalId, TypeInScopePage typeInScopePage,
                                     MediaTypeInScopePage mediaTypeInScopePage, File savedFile){
        IPathName iPathName = null;

        if( typeInScopePage == TypeInScopePage.SCOPE ){

            iPathName = scopeRepository.getReferenceById( generalId );

            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
                ((Scope)iPathName).getImages().add(savedFile);
            }
            else if(mediaTypeInScopePage == MediaTypeInScopePage.ICON) {
                if(((Scope)iPathName).getIcon() != null) {
                    Long oldIconId = ((Scope) iPathName).getIcon().getId();
                    fileDbRepository.deleteById( oldIconId );
                }

                ((Scope)iPathName).setIcon(savedFile);
            }
            else if( mediaTypeInScopePage == MediaTypeInScopePage.DISTRIBUTION_AGREEMENT){
                if(((Scope)iPathName).getDistributionAgreement() != null) {
                    Long oldDistrAgrId = ((Scope) iPathName).getDistributionAgreement().getId();
                    fileDbRepository.deleteById( oldDistrAgrId );
                }

                ((Scope)iPathName).setDistributionAgreement(savedFile);
            }
            else throw new IllegalArgumentException();

            scopeRepository.save((Scope)iPathName);

        }
        else if( typeInScopePage == TypeInScopePage.FOLDER ){

            iPathName = folderRepository.getReferenceById( generalId );

            if(mediaTypeInScopePage == MediaTypeInScopePage.MANIFEST) {
                if(((Folder)iPathName).getManifestForIOSFile() != null) {
                    Long oldManifestId = ((Folder) iPathName).getManifestForIOSFile().getId();
                    fileDbRepository.deleteById( oldManifestId );
                }

                ((Folder)iPathName).setManifestForIOSFile(savedFile);
            }
            else throw new IllegalArgumentException();

            folderRepository.save((Folder)iPathName);
        }
        else if( typeInScopePage == TypeInScopePage.VERSION ){
            iPathName = versionRepository.getReferenceById( generalId );

            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
                ((Version)iPathName).getImages().add(savedFile);
            }
            else if(mediaTypeInScopePage == MediaTypeInScopePage.FILE) {
                ((Version)iPathName).getFiles().add(savedFile);
            }
            else throw new IllegalArgumentException();

            versionRepository.save((Version) iPathName);
        }
        else throw new IllegalArgumentException();
    }

    @Transactional
    public void delete ( Long fileId ) {
        try {
            if(fileDbRepository.existsById(fileId)) {
                File fileDb = fileDbRepository.getReferenceById(fileId);
                String location = fileDb.getLocation();

                fileDbRepository.deleteById(fileId );//удаляет в бд
                fileSystemRepository.delete(location);//удаляет с сервера
            }
            else {
                throw new FileSystemNotFoundException("Файл не найден в БД");
            }
        }
        catch (IllegalArgumentException | FileSystemNotFoundException e){
            throw e;
        }

    }

    public FileSystemResource get ( Long fileId ) {
        File file = fileDbRepository.findById( fileId )// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }

}
