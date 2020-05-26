package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.converters.AuthorConverter;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorDto> getMessages() {
        return authorRepository.findAll().stream()
                .map(AuthorConverter::toDto)
                .collect(Collectors.toList());
    }
}
