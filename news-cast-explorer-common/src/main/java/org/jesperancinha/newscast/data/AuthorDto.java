package org.jesperancinha.newscast.data;

import lombok.Builder;

import java.util.List;
import java.util.Objects;

public record AuthorDto(
        Long id,
        String newsCastId,
        Long createdAt,
        String name,
        String userName,
        List<MessageDto> messages
) {
    @Builder
    public AuthorDto {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDto authorDto)) return false;
        return newsCastId.equals(authorDto.newsCastId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsCastId);
    }
}
