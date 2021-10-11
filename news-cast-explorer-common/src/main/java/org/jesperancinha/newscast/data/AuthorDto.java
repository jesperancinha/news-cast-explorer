package org.jesperancinha.newscast.data;

import lombok.Builder;

import java.util.List;

public record AuthorDto(
        String id,
        Long createdAt,
        String name,
        String screenName,
        List<MessageDto> messages
) {
    @Builder
    public AuthorDto {
    }
}
