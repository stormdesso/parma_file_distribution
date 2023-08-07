package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.service.GuestPageService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class GuestPageController {

    private final GuestPageService guestPageService;

    @GetMapping("/v/{version_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByVersionId(@PathVariable Long version_id) throws IOException {
        return guestPageService.getGuestPageByVersionId(version_id);
    }

    @GetMapping("/s/{scope_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByScopeId(@PathVariable Long scope_id) throws IOException {
        return guestPageService.getGuestPageByScopeId( scope_id);
    }
}
