package org.jesperancinha.newscast.service;

import org.jesperancinha.newscast.converters.AuthorConverter;
import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getMessages() {
        return authorRepository.findAll().stream()
                .map(AuthorConverter::toDto)
                .collect(Collectors.toList());
    }
}
