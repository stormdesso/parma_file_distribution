package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.service.CustomUserDetailsService;
import ru.parma.filesdistr.service.GuestPageService;
import ru.parma.filesdistr.service.ScopeAccessService;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
@RequestMapping("guest_page")
public class GuestPageController {

    private final GuestPageService guestPageService;

    private final ScopeAccessService scopeAccessService;


    @GetMapping("v/{version_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByVersionId(@PathVariable Long version_id) throws AccessDeniedException {
        scopeAccessService.tryGetAccess(TypeInScopePage.VERSION, version_id, CustomUserDetailsService.getAuthorizedUserId());
        return guestPageService.getGuestPageByVersionId( version_id);
    }

    @GetMapping("s/{scope_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByScopeId(@PathVariable Long scope_id) throws AccessDeniedException {
        scopeAccessService.tryGetAccess(TypeInScopePage.SCOPE, scope_id, CustomUserDetailsService.getAuthorizedUserId());
        return guestPageService.getGuestPageByScopeId( scope_id);
    }
}
