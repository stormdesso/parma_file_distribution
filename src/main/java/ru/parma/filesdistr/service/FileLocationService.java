package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.enums.MediaTypeInAdminPage;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.FileMapper;
import ru.parma.filesdistr.models.*;
import ru.parma.filesdistr.repos.*;
import ru.parma.filesdistr.utils.IPathName;
import ru.parma.filesdistr.utils.Utils;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.text.ParseException;
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
    final TagRepository tagRepository;
    private final AdminPageAccessService adminPageAccessService;


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
                Optional<Scope> scopeOptional = scopeRepository.findById(generalId);
                if (!scopeOptional.isPresent()) {
                    throw new EntityNotFoundException(String.format("Scope с id %d  не найден", generalId));
                }
                String fullpath = scopeOptional.get().getRootPath();
                location = fileSystemRepository.saveInScope(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.FOLDER ){
                Optional<Folder> folderOptional = folderRepository.findById(generalId);
                if (!folderOptional.isPresent()) {
                    throw new EntityNotFoundException(String.format("Folder с id %d  не найден", generalId));
                }
                String fullpath = folderOptional.get().getRootPath();
                location = fileSystemRepository.saveInFolder(bytes, fileName, mediaTypeInScopePage, fullpath);
            }
            else if( typeInScopePage == TypeInScopePage.VERSION ){
                Optional<Version> versionOptional = versionRepository.findById(generalId);
                if (!versionOptional.isPresent()) {
                    throw new EntityNotFoundException(String.format("Version с id %d  не найден", generalId));
                }
                String fullpath = versionOptional.get().getRootPath();
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
                Tag tag;
                if(tagId != null) {
                    Optional<Tag> tagOptional = tagRepository.findById( tagId );
                    if (!tagOptional.isPresent()) {
                        throw new EntityNotFoundException(String.format("Tag с id %d  не найден", tagId));
                    }
                    tag = tagOptional.get();
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
        IPathName iPathName;

        if( typeInScopePage == TypeInScopePage.SCOPE ){

            Optional<Scope> scopeOptional = scopeRepository.findById( generalId );
            if (!scopeOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Scope с id %d  не найден", generalId));
            }
            iPathName = scopeOptional.get();
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

            Optional<Folder> folderOptional = folderRepository.findById( generalId );
            if (!folderOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Folder с id %d  не найден", generalId));
            }
            iPathName = folderOptional.get();
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
            Optional<Version> versionOptional = versionRepository.findById( generalId );
            if (!versionOptional.isPresent()) {
                throw new EntityNotFoundException(String.format("Version с id %d  не найден", generalId));
            }
            iPathName = versionOptional.get();
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


    public FileDto saveOnAdminPage(Long updatedUserId, MultipartFile multipartFile, MediaTypeInAdminPage mediaTypeInAdminPage, String filetype)
            throws ParseException, IOException{
        Date currDate = Utils.getDateWithoutTime();
        String location = null;
        try {
            User updatedUser = adminPageAccessService.getUserById (updatedUserId);
            String fullpath = updatedUser.getRootPath();

            location = fileSystemRepository.saveInAdminPage (fullpath, mediaTypeInAdminPage, multipartFile);

            File file = File
                    .builder()
                    .name(multipartFile.getOriginalFilename ())
                    .size(Utils.convertByteToMb(multipartFile.getBytes ()))
                    .type(filetype)
                    .dateCreated(currDate)
                    .location(location)
                    .build();

            if(mediaTypeInAdminPage == MediaTypeInAdminPage.PROFILE_PICTURE){
                //TODO: возможно, надо удалять передсохранением
                updatedUser.setProfilePicture (file);
            }
            userRepository.save (updatedUser);

            return FileMapper.INSTANCE.toFileDto(file);
        }catch (Exception e){
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

    @Transactional
    public void delete ( Long fileId ) {
        try {
            if(fileDbRepository.existsById(fileId)) {
                Optional<File> fileDbOptional = fileDbRepository.findById(fileId);

                String location = fileDbOptional.get().getLocation();

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
