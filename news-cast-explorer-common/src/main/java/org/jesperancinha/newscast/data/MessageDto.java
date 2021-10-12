package org.jesperancinha.newscast.data;

import lombok.Builder;

import java.util.Objects;

public record MessageDto(
        Long id,
        String newsCastId,
        Long createdAt,
        String text
) {
    @Builder
    public MessageDto {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageDto that)) return false;
        return newsCastId.equals(that.newsCastId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newsCastId);
    }
}
