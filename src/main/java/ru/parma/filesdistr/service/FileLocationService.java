package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.SavedFileDto;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.SavedFileMapper;
import ru.parma.filesdistr.models.*;
import ru.parma.filesdistr.repos.*;
import ru.parma.filesdistr.utils.IPathName;
import ru.parma.filesdistr.utils.Utils;

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

    private boolean getAccess ( Scope scope, @NotNull User user){
        return user.getAvailableScopes().contains(scope);
    }
    public boolean tryGetAccess ( TypeInScopePage typeInScopePage, Integer generalId, @NotNull Integer userId){

            User user = userRepository.getReferenceById(userId.longValue());

            if( typeInScopePage == TypeInScopePage.SCOPE ){
                Scope scope = scopeRepository.getReferenceById(generalId.longValue());
                return getAccess(scope, user);
            }
            else if( typeInScopePage == TypeInScopePage.FOLDER ){
                Folder folder = folderRepository.getReferenceById(generalId.longValue());
                return getAccess(folder.getScope(), user);
            }
            else if( typeInScopePage == TypeInScopePage.VERSION ){
                Version version = versionRepository.getReferenceById(Long.valueOf(generalId));
                return getAccess(version.getFolder().getScope(), user);
            }


        return false;
    }

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


    @Transactional
    public void delete ( Long fileId ) {
        File fileDb = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String location = fileDb.getLocation();
        fileDbRepository.delete(fileDb);//удаляет в бд
        fileSystemRepository.delete(location);//удаляет с сервера
    }

    public FileSystemResource get ( Long fileId ) {
        File file = fileDbRepository.findById(Math.toIntExact(fileId))// parma.File
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }

}
