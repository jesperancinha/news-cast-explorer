package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PageDto {

    private Long duration;

    private Long createdAt;

    private List<AuthorDto> authors;
}
