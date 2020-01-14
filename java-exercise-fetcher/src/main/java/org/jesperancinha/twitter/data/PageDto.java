package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PageDto {

    private Long duration;

    private Long createdAt;

    private List<AuthorDto> authors;
}
