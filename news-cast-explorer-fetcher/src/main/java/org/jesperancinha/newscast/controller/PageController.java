package org.jesperancinha.newscast.controller;

import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.service.PageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pages")
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping
    public List<PageDto> getAllPages() {
        return this.pageService.getAllPages();
    }
}
