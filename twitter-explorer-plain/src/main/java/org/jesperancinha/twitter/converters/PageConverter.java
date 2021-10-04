package org.jesperancinha.twitter.converters;

import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.db.Page;

import java.util.stream.Collectors;

public class PageConverter {
    public static PageDto toDto(Page page) {
        return PageDto.builder()
                .createdAt(page.getCreatedAt())
                .duration(page.getDuration())
                .authors(page.getAuthors().stream().map(AuthorConverter::toDto).collect(Collectors.toList()))
                .build();
    }

    public static Page toData(PageDto pageDto) {
        return Page.builder()
                .createdAt(pageDto.getCreatedAt())
                .duration(pageDto.getDuration())
                .build();
    }
}
