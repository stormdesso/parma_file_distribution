package ru.parma.filesdistr.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.parma.filesdistr.dto.TagDto;
import ru.parma.filesdistr.mappers.FileMapper;
import ru.parma.filesdistr.models.Tag;
import ru.parma.filesdistr.repos.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppearanceService {
    private final TagRepository tagRepository;

    public List<TagDto> getAll ()
    {
       return FileMapper.INSTANCE.toTagDtos (tagRepository.findAll());
    }
    public void add (@NotNull TagDto tagDto)
    {
        Tag tag = Tag.builder ()
                .letter (tagDto.getLetter ())
                .backgroundColor (tagDto.getBackgroundColor ())
                .letterColor (tagDto.getLetterColor ())
                .build ();
        tagRepository.save (tag);
    }

    public void delete(Long tagId)
    {
        Optional<Tag> tag =  tagRepository.findById (tagId);
        if(tag.isPresent ()){
            tagRepository.deleteById (tagId);
        }
        else {
            //TODO:exception
        }
    }
}
