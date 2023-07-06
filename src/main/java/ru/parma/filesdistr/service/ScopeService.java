package ru.parma.filesdistr.service;

import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.ScopeDto;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScopeService {
    public List<ScopeDto> get(long userId) {
        return new ArrayList<>();
    }
}
