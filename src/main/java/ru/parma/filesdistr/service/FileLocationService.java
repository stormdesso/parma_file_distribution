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
                Folder folder = folderRepository.getReferenceById((long) generalId);
                String fullpath = folder.getRootPath();
                location = fileSystemRepository.saveInFolder(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.VERSION ){
                String fullpath = versionRepository.getReferenceById((long) generalId).getRootPath();
                location = fileSystemRepository.saveInVersion(bytes, fileName, mediaTypeInScopePage, fullpath);
            }

            File file = File
                    .builder()
                    .name(fileName)
                    .size(Utils.convertByteToMb(bytes))
                    .type(filetype)
                    .dateCreated(currDate)
                    .location(location)
                    .build();
            File savedFile = fileDbRepository.save(file);// сохраняет в БД

//            if(!trySaveIntermediateEntity(typeInScopePage, mediaTypeInScopePage, generalId, savedFile)) {
//                throw new Exception();
//            }// сохраняет в БД

            return SavedFileMapper.INSTANCE.toSaveFileDto(savedFile);

        } catch (Exception e) {
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

//    @Transactional
//    boolean trySaveIntermediateEntity ( TypeInScopePage typeInScopePage, MediaTypeInScopePage mediaTypeInScopePage,
//                                        int generalId, File savedFile )
//            throws EntityNotFoundException {
//
//        if(typeInScopePage == TypeInScopePage.VERSION) {
//            if(mediaTypeInScopePage == MediaTypeInScopePage.FILE) {
//                FileVersion fileVersion = FileVersion
//                        .builder()
//                        .versionId(generalId)
//                        .fileId(savedFile.getId())
//                        .build();
//                fileVersionRepository.save(fileVersion);
//                return true;
//            }
//
//            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
//                IllustrationVersion illustrationVersion = IllustrationVersion
//                        .builder()
//                        .versionId(generalId)
//                        .fileId(savedFile.getId())
//                        .build();
//                illustrationVersionRepository.save(illustrationVersion);
//                return true;
//            }
//        }else if(typeInScopePage == TypeInScopePage.FOLDER) {
//            if(mediaTypeInScopePage == MediaTypeInScopePage.MANIFEST) {
//
//                ManifestIOSFolder oldEntity = manifestIOSFolderRepository.findByFolderId(generalId);
//                if(oldEntity != null) {
//                    manifestIOSFolderRepository.delete(oldEntity);
//                }
//
//                ManifestIOSFolder manifestIOSFolder = ManifestIOSFolder
//                        .builder()
//                        .folderId(generalId)
//                        .fileId(savedFile.getId())
//                        .build();
//                manifestIOSFolderRepository.save(manifestIOSFolder);
//                return true;
//            }
//        }else if(typeInScopePage == TypeInScopePage.SCOPE) {
//            if(mediaTypeInScopePage == MediaTypeInScopePage.ICON) {
//                Scope scope = scopeRepository.getReferenceById((long) generalId);
//                if(scope.getIconId() != null) {
//                    delete(scope.getIconId());// delete old icon from table "file"
//                }
//                scope.setIconId(Long.valueOf(savedFile.getId()));
//                scopeRepository.save(scope);
//                return true;
//            }
//            if(mediaTypeInScopePage == MediaTypeInScopePage.ILLUSTRATION) {
//                IllustrationScope illustrationScope = IllustrationScope
//                        .builder()
//                        .scopeId(generalId)
//                        .fileId(savedFile.getId())
//                        .build();
//                illustrationScopeRepository.save(illustrationScope);
//                return true;
//            }
//            if(mediaTypeInScopePage == MediaTypeInScopePage.DISTRIBUTION_AGREEMENT) {
//
//                LicenseAgreementFileForScope oldEntity = licenseAgreementFileForScopeRepository.findByScopeId(generalId);
//                if(oldEntity != null) {
//                    long oldFileId = oldEntity.getFileId();
//                    oldEntity.setFileId(savedFile.getId());
//                    delete(oldFileId);
//                }
//                else{
//                    LicenseAgreementFileForScope licenseAgreementFileForScope = LicenseAgreementFileForScope
//                            .builder()
//                            .scopeId(generalId)
//                            .fileId(savedFile.getId())
//                            .build();
//                    licenseAgreementFileForScopeRepository.save(licenseAgreementFileForScope);
//                }
//                return true;
//            }
//        }
//
//
//        return false;
//    }
//


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
