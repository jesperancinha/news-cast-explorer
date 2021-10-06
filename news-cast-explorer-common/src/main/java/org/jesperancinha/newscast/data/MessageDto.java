package org.jesperancinha.newscast.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class MessageDto {
    private final String id;

    @EqualsAndHashCode.Exclude
    private final Long createdAt;

    @EqualsAndHashCode.Exclude
    private final String text;
}
