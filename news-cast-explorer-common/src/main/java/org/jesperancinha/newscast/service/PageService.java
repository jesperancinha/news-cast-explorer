package org.jesperancinha.newscast.service;

import org.jesperancinha.newscast.converters.PageConverter;
import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.model.explorer.Page;
import org.jesperancinha.newscast.repository.AuthorRepository;
import org.jesperancinha.newscast.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PageService {

    private final PageRepository pageRepository;
    private final AuthorRepository authorRepository;

    public PageService(PageRepository pageRepository, AuthorRepository authorRepository) {
        this.pageRepository = pageRepository;
        this.authorRepository = authorRepository;
    }

    public List<PageDto> getAllPages() {
        final List<Page> pageList = pageRepository
                .findAll();
        pageList.forEach(page -> page.getAuthors().forEach(author -> {
            final Long authorId = author.getId();
            final Optional<Author> authorOptional = authorRepository.findById(authorId);
            authorOptional.ifPresent(value -> author.getMessages().addAll(value.getMessages()));
        }));
        return pageList
                .stream()
                .map(PageConverter::toDto).collect(Collectors.toList());
    }

    public PageDto create(PageDto pageDto) {
        return PageConverter.toDto(pageRepository.save(PageConverter.toData(pageDto)));
    }

    public Optional<Page> findPageById(Long idPage) {
        return pageRepository.findById(idPage);
    }
}
