package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parma.filesdistr.dto.ViewPageDto;
import ru.parma.filesdistr.service.ViewPageService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class ViewPageController {

    private final ViewPageService viewPageService;

    @GetMapping("/scope/{scope_id}/version/{version_id}")
    @ResponseBody
    public ViewPageDto getViewPageByScopeId(@PathVariable Long scope_id, @PathVariable String version_id) throws IOException {
        return viewPageService.getViewPage(scope_id, version_id);
    }
}
