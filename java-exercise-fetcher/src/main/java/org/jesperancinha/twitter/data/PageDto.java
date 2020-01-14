package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class PageDto {

    private Long duration;

    private List<AuthorDto> authors;
}
