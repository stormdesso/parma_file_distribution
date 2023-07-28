package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.repos.VersionRepository;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;
}
