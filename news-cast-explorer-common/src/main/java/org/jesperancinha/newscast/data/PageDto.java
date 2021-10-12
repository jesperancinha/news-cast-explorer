package org.jesperancinha.newscast.data;

import lombok.Builder;

import java.util.List;

public record PageDto(
        Long id,
        Long duration,
        Long createdAt,
        List<AuthorDto> authors
) {
    @Builder
    public PageDto {
    }
}
