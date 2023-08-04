package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.parma.filesdistr.dto.GuestPageDto;
import ru.parma.filesdistr.enums.Roles;
import ru.parma.filesdistr.enums.TypeInScopePage;
import ru.parma.filesdistr.service.CustomUserDetailsService;
import ru.parma.filesdistr.service.GuestPageService;
import ru.parma.filesdistr.service.ScopeAccessService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("guest_page")
public class GuestPageController {

    private final GuestPageService guestPageService;

    private final ScopeAccessService scopeAccessService;


    @GetMapping("v/{version_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByVersionId(@PathVariable Long version_id) throws IOException {
        String role =  SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
        if (EnumUtils.isValidEnum(Roles.class, role)) {
            scopeAccessService.tryGetAccess(TypeInScopePage.VERSION, version_id,  CustomUserDetailsService.getAuthorizedUserId());
        }
        else {
            HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
            response.sendRedirect("/login");
        }
        return guestPageService.getGuestPageByVersionId(version_id);
    }

    @GetMapping("s/{scope_id}")
    @ResponseBody
    public GuestPageDto getGuestPageByScopeId(@PathVariable Long scope_id) throws IOException {
        String role =  SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().get().toString();
        if (EnumUtils.isValidEnum(Roles.class, role)) {
            scopeAccessService.tryGetAccess(TypeInScopePage.SCOPE, scope_id, CustomUserDetailsService.getAuthorizedUserId());
        }
        else {
            HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
            response.sendRedirect("/login");
        }
        return guestPageService.getGuestPageByScopeId( scope_id);
    }
}
