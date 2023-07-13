package ru.parma.filesdistr.service;

import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.VersionDto;
import ru.parma.filesdistr.models.Version;
import ru.parma.filesdistr.repos.VersionRepository;

@Service
public class VersionService {

    final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    private VersionDto convertEntityToDto(Version version){
        VersionDto versionDto = new VersionDto();
        //TODO добавить поля для маппинга
        return versionDto;
    }

}
