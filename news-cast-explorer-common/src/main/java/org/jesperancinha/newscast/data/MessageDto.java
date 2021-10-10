package org.jesperancinha.newscast.data;

import lombok.Builder;

public record MessageDto(
        String id,
        Long createdAt,
        String text
) {
    @Builder
    public MessageDto {
    }
}
