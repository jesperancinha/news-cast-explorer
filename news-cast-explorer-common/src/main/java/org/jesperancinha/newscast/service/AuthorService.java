package org.jesperancinha.newscast.service;

import org.jesperancinha.newscast.converters.AuthorConverter;
import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorConverter::toDto)
                .collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(Long authorId) {
        return AuthorConverter.toDto(authorRepository.findById(authorId).orElse(null));
    }

    public Optional<Author> findAuthorById(Long authorId){
        return authorRepository.findById(authorId);
    }
}
