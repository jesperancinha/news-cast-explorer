package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageDto {

    private final Long duration;

    private final Long createdAt;

    private final List<AuthorDto> authors;
}
