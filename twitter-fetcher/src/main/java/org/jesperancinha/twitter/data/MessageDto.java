package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@EqualsAndHashCode
@Getter
public class MessageDto {
    private String id;

    @EqualsAndHashCode.Exclude
    private Long createdAt;

    @EqualsAndHashCode.Exclude
    private String text;
}
