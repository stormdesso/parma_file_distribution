package ru.parma.filesdistr.service;


import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.parma.filesdistr.aop.annotations.LoggableMethod;
import ru.parma.filesdistr.aop.exceptions.EntityIllegalArgumentException;
import ru.parma.filesdistr.aop.exceptions.EntityNotFoundException;
import ru.parma.filesdistr.aop.exceptions.FileSystemException;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.enums.MediaTypeInAdminPage;
import ru.parma.filesdistr.enums.MediaTypeInScopePage;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.mappers.FileMapper;
import ru.parma.filesdistr.models.*;
import ru.parma.filesdistr.repos.*;
import ru.parma.filesdistr.utils.IPathName;
import ru.parma.filesdistr.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class FileLocationService {
    private final FileSystemRepository fileSystemRepository;
    private final FileRepository fileDbRepository;
    private final ScopeRepository scopeRepository;
    private final FolderRepository folderRepository;
    private final VersionRepository versionRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;


    private final VersionService versionService;


    @Transactional
    @LoggableMethod
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
            else throw new EntityIllegalArgumentException(String.format("Wrong TypeInScopePage: %s", typeInScopePage.toString()));

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

            attachFileToEntity (generalId,  typeInScopePage, mediaTypeInScopePage,  savedFile);
            return FileMapper.INSTANCE.toFileDto(savedFile);

        } catch (Exception e) {
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw e;
        }
    }

    @Transactional
    @LoggableMethod
    public FileDto save(Long updatedUserId, MultipartFile multipartFile, MediaTypeInAdminPage mediaTypeInAdminPage, String filetype) {
        String location = null;
        try {
            Date currDate = Utils.getDateWithoutTime();
            User updatedUser = userRepository.findUserWithoutRoot (updatedUserId);

            location = fileSystemRepository.saveInAdminPage (updatedUser.getRootPath(), mediaTypeInAdminPage, multipartFile);

            File file = File
                    .builder()
                    .name(multipartFile.getOriginalFilename ())
                    .size(Utils.convertByteToMb(multipartFile.getBytes ()))
                    .type(filetype)
                    .dateCreated(currDate)
                    .location(location)
                    .build();

            File savedFile = fileDbRepository.save(file);

            attachFileToEntity (updatedUser, mediaTypeInAdminPage, savedFile);

            return FileMapper.INSTANCE.toFileDto(savedFile);
        } catch (Exception e){
            // убираем за собой
            if(location != null && !location.isEmpty()) {
                fileSystemRepository.delete(location);
            }
            throw new FileSystemException(e.getMessage());
        }
    }

    @Transactional
    @LoggableMethod
    protected void attachFileToEntity (long generalId, TypeInScopePage typeInScopePage,
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
                    delete (oldIconId);
                }

                ((Scope)iPathName).setIcon(savedFile);
            }
            else if( mediaTypeInScopePage == MediaTypeInScopePage.DISTRIBUTION_AGREEMENT){
                if(((Scope)iPathName).getDistributionAgreement() != null) {
                    Long oldDistrAgrId = ((Scope) iPathName).getDistributionAgreement().getId();
                    delete (oldDistrAgrId);
                }

                ((Scope)iPathName).setDistributionAgreement(savedFile);
            }
            else throw new EntityIllegalArgumentException(String.format("Wrong MediaTypeInScopePage: %s", mediaTypeInScopePage.toString()));

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
                    delete (oldManifestId);
                }

                ((Folder)iPathName).setManifestForIOSFile(savedFile);
            }
            else throw new EntityIllegalArgumentException(String.format("Wrong MediaTypeInScopePage: %s", mediaTypeInScopePage.toString()));

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
            else throw new EntityIllegalArgumentException(String.format("Wrong MediaTypeInScopePage: %s", mediaTypeInScopePage.toString()));

            versionRepository.save((Version) iPathName);
        }
        else throw new EntityIllegalArgumentException(String.format("Wrong TypeInScopePage: %s", typeInScopePage.toString()));
    }

    @Transactional
    @LoggableMethod
    protected void attachFileToEntity (User user, MediaTypeInAdminPage mediaTypeInAdminPage, File savedFile){
        if( mediaTypeInAdminPage == MediaTypeInAdminPage.PROFILE_PICTURE ){
                if(user.getProfilePicture () != null) {
                    Long oldIconId = user.getProfilePicture ().getId ();
                    delete ( oldIconId );
                }
                user.setProfilePicture (savedFile);
            userRepository.save(user);
        }
        else throw new EntityIllegalArgumentException(String.format
                ("Wrong MediaTypeInAdminPage: %s", mediaTypeInAdminPage.toString()));
    }

    @Transactional
    @LoggableMethod
    public void delete ( Long fileId ) {
        try {
            if(fileDbRepository.existsById(fileId)) {
                Optional<File> fileDbOptional = fileDbRepository.findById(fileId);

                String location = fileDbOptional.get().getLocation();

                fileDbRepository.deleteById(fileId );//удаляет в бд
                fileSystemRepository.delete(location);//удаляет с сервера
            }
            else {
                throw new FileSystemException("Файл не найден в БД");
            }
        }
        catch (Exception e){
            //?
            throw new EntityNotFoundException("File", fileId);
        }

    }

    @LoggableMethod
    public FileSystemResource get ( Long fileId ) {
        File file = fileDbRepository.findById( fileId )// parma.File
                .orElseThrow(() -> new EntityNotFoundException("File", fileId));
        return fileSystemRepository.findInFileSystem(file.getLocation());
    }

    public InputStream getAsByteArray (Long fileId ) throws IOException {
        return Files.newInputStream (get (fileId).getFile ().toPath ());
    }

    private @NotNull List<java.io.File> getFiles (@NotNull List<File> fileList) {
        List<java.io.File> files = new ArrayList<> ();
        for (File file: fileList) {
            files.add (fileSystemRepository.findInFileSystem(file.getLocation()).getFile ());
        }
        return files;
    }

    public List<java.io.File> getFiles (Long versionId ) {
        Version version = versionService.get (versionId);
        return getFiles (version.getFiles ());
    }

    @LoggableMethod
    public byte[] getZipArchive(@NotNull List<java.io.File> files) {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteOutputStream);
        try {
            for(java.io.File file: files) {
                zipOutputStream.putNextEntry(new ZipEntry (file.getName ()));
                FileInputStream fileInputStream = new FileInputStream(file);
                IOUtils.copy(fileInputStream, zipOutputStream);
                fileInputStream.close();
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
        } catch (IOException e) {
            throw new FileSystemException(e.getMessage());
        }
        return byteOutputStream.toByteArray();
    }
}
