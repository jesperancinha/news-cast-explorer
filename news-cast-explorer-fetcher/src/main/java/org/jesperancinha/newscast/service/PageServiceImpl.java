package org.jesperancinha.newscast.service;

import org.jesperancinha.newscast.converters.PageConverter;
import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl {

    private final PageRepository pageRepository;

    public PageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    public List<PageDto> getAllPages() {
        return pageRepository
                .findAll()
                .stream()
                .map(PageConverter::toDto).collect(Collectors.toList());
    }

    public PageDto create(PageDto pageDto) {
        return PageConverter.toDto(pageRepository.save(PageConverter.toData(pageDto)));
    }
}
