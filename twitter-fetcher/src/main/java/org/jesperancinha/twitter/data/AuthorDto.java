package org.jesperancinha.twitter.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Builder
@EqualsAndHashCode
@Data
public class AuthorDto {
    private String id;

    @EqualsAndHashCode.Exclude
    private Long createdAt;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String screenName;

    @EqualsAndHashCode.Exclude
    private List<MessageDto> messageDtos;
}
