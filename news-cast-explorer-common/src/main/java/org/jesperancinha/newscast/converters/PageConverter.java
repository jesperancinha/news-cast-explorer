package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.model.explorer.Page;

import java.util.stream.Collectors;

public class PageConverter {
    public static PageDto toDto(Page page) {
        return PageDto.builder()
                .id(page.getId())
                .createdAt(page.getCreatedAt())
                .duration(page.getDuration())
                .authors(page.getAuthors().stream().map(AuthorConverter::toDto).collect(Collectors.toList()))
                .build();
    }

    public static Page toData(PageDto pageDto) {
        return Page.builder()
                .createdAt(pageDto.createdAt())
                .duration(pageDto.duration())
                .build();
    }
}
