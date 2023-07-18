package ru.parma.filesdistr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.parma.filesdistr.dto.ViewScopeDto;
import ru.parma.filesdistr.service.ViewScopeService;

@Controller
@RequiredArgsConstructor
@RequestMapping("view")
public class ViewController {

    private final ViewScopeService viewScopeService;

    @GetMapping("/s/{scope_id}/v/{version_id}")
    @ResponseBody
    public ViewScopeDto get(@PathVariable Long scope_id, @PathVariable Long version_id) {
        return viewScopeService.getViewScopeByScopeIdAndVersionId(scope_id, version_id);
    }

}
