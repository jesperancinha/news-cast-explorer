package org.jesperancinha.newscast.controller;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.service.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAuthors();
    }

    @GetMapping("/{authorId}")
    public AuthorDto getAllAuthorsByMessageId(@PathVariable Long authorId) {
        return authorService.getAuthorById(authorId);
    }
}
