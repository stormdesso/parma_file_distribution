package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.LicenseAgreementFileForScopeRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScopeService {

    private final ScopeRepository scopeRepository;
    private final LicenseAgreementFileForScopeRepository licenseAgreementFileForScopeRepository;

    public List<ScopeDto> getAll() {

        return scopeRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    public ScopeDto getScopeById(Long scopeId) {
        return convertEntityToDto(scopeRepository.getReferenceById(scopeId));
    }

    //TODO:заменить на маппер
    private ScopeDto convertEntityToDto(Scope scope){
        ScopeDto scopeDto = new ScopeDto();
        scopeDto.setId(scope.getId());
        scopeDto.setName(scope.getName());
        scopeDto.setDescription(scope.getDescription());
        scopeDto.setCopyright(scope.getCopyright());
        scopeDto.setShowIllustration(scope.isShowIllustration());
        scopeDto.setIcon(scope.getIcon());
        //TODO !!!не забыть заменить!!!
        scopeDto.setImages(new ArrayList<>());
        scopeDto.setLicenseAgreement(licenseAgreementFileForScopeRepository.findByScopeId(Math.toIntExact(scope.getId())).getContent());
        //TODO !!!не забыть заменить!!!
        scopeDto.setFolders(new ArrayList<>());

        return scopeDto;
    }

}
