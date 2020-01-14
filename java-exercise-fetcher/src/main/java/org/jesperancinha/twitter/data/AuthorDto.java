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
