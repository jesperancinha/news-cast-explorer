package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.data.PageDto;

import java.util.List;

public interface PageService {
    List<PageDto> getMessages();

    PageDto create(PageDto pageDto);
}
