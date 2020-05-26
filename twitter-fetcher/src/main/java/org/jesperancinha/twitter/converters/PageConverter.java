package org.jesperancinha.twitter.converters;

import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.db.Page;

public class PageConverter {
    public static PageDto toDto(Page page) {
        return PageDto.builder()
                .createdAt(page.getCreatedAt())
                .duration(page.getDuration())
                .build();
    }

    public static Page toData(PageDto pageDto) {
        return Page.builder()
                .createdAt(pageDto.getCreatedAt())
                .duration(pageDto.getDuration())
                .build();
    }
}
