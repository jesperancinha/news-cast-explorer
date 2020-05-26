package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class MessageDto {
    private final String id;

    @EqualsAndHashCode.Exclude
    private final Long createdAt;

    @EqualsAndHashCode.Exclude
    private final String text;
}
