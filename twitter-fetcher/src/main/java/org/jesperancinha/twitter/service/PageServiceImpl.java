package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.converters.PageConverter;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.repository.PageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    public PageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    public List<PageDto> getMessages() {
        return pageRepository
                .findAll()
                .stream()
                .map(PageConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public PageDto create(PageDto pageDto) {
        return PageConverter.toDto(pageRepository.save(PageConverter.toData(pageDto)));
    }
}
