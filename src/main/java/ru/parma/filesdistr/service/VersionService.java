package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.VersionRepository;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final VersionRepository versionRepository;

    private VersionDto convertEntityToDto(Version version){
        VersionDto versionDto = new VersionDto();
        //TODO добавить поля для маппинга
        return versionDto;
    }

}
