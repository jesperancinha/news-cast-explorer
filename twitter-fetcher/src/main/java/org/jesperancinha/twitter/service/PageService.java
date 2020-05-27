package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.data.PageDto;

import java.util.List;

public interface PageService {
    List<PageDto> getAllPages();

    PageDto create(PageDto pageDto);
}
