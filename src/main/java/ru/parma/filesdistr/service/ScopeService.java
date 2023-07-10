package ru.parma.filesdistr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.FileDto;
import ru.parma.filesdistr.dto.ScopeDto;
import ru.parma.filesdistr.models.Scope;
import ru.parma.filesdistr.repos.LicenseAgreementFileForScopeRepository;
import ru.parma.filesdistr.repos.ScopeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScopeService {

    @Autowired
    ScopeRepository scopeRepository;
    @Autowired
    LicenseAgreementFileForScopeRepository licenseAgreementFileForScopeRepository;

    public List<ScopeDto> getAll() {
        return scopeRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    public ScopeDto getScopeById(Long scopeId) {
        return convertEntityToDto(scopeRepository.getScopeById(scopeId));
    }

    private ScopeDto convertEntityToDto(Scope scope){
        ScopeDto scopeDto = new ScopeDto();
        scopeDto.setId(scope.getId());
        scopeDto.setName(scope.getName());
        scopeDto.setDescription(scope.getDescription());
        scopeDto.setCopyright(scope.getCopyright());
        scopeDto.setShowIllustration(scope.isShowIllustration());
        scopeDto.setIcon(scope.getIcon());
        scopeDto.setImages(new ArrayList<FileDto>());//!!!не забыть заменить!!!
        scopeDto.setLicenseAgreement(licenseAgreementFileForScopeRepository.findByScopeId(Math.toIntExact(scope.getId())).getContent());
        scopeDto.setFolders(new ArrayList<>());//!!!не забыть заменить!!!

        return scopeDto;
    }

}
