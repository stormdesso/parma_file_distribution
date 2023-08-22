package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.mappers.VersionMapper;
import ru.parma.filesdistr.models.Folder;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.FileSystemRepository;
import ru.parma.filesdistr.repos.FolderRepository;
import ru.parma.filesdistr.repos.VersionRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    private final FolderRepository folderRepository;

    private final FileSystemRepository fileSystemRepository;

    public List<VersionDto> getAll(long folder_id) {
        Optional<Folder> folderOptional = folderRepository.findById(folder_id);
        if (!folderOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Папки с id %d не существует", folder_id));
        }
        return VersionMapper.INSTANCE.toVersionDtos(folderOptional.get().getVersions());
    }

    public VersionDto get(long version_id) {
        Optional<Version> versionOptional = versionRepository.findById(version_id);
        if (!versionOptional.isPresent()) {
            throw new EntityNotFoundException( String.format("Версии с id %d не существует", version_id));
        }
        return VersionMapper.INSTANCE.toVersionDto(versionOptional.get());
    }

    public void add(VersionDto versionDto, Long folder_id) {
        checkDto(versionDto);
        Optional<Folder> folderOptional = folderRepository.findById(folder_id);
        if (!folderOptional.isPresent()) {
            throw new EntityNotFoundException(String.format("Папки с id %d не существует", folder_id));
        }
        Folder folder = folderOptional.get();
        Version version = VersionMapper.INSTANCE.toVersion(versionDto);
        version.setFolder(folder);
        List<Version> versions = folder.getVersions();
        versions.add(version);
        versionRepository.save(version);

    }

    public void update(VersionDto versionDto) {
        checkDto(versionDto);
        Optional<Version> existedVersion = versionRepository.findById(versionDto.getId());
        if (!existedVersion.isPresent()) {
            throw new  EntityNotFoundException("Такой версии для обновления не существует");
        }
        Version version = VersionMapper.INSTANCE.toVersion(versionDto);
        versionRepository.save(version);
    }

    private void checkDto(VersionDto versionDto) {
        if (versionDto == null) {
            throw new IllegalArgumentException("Создаваемый объект не может быть null");
        }
        if (versionDto.getVersionNumber() == null) {
            throw new IllegalArgumentException("Номер версии не может быть null");
        }
        if (versionDto.getDateOfPublication() == null) {
            throw new IllegalArgumentException("Дата публикации не может быть null");
        }
    }

    public void delete(Long version_id) {
        Optional<Version> versionOptional = versionRepository.findById(version_id);
        if (!versionOptional.isPresent()) {
            throw new EntityNotFoundException( String.format("Версии с id %d не существует", version_id));
        }
        Version version = versionOptional.get();
        fileSystemRepository.delete(version.getRootPath());
        versionRepository.delete(version);
    }
}
