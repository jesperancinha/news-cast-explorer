package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.model.explorer.Page;

import java.util.Objects;
import java.util.stream.Collectors;

public class AuthorConverter {
    public static AuthorDto toDto(Author author) {
        if (Objects.isNull(author)) {
            return null;
        }
        return AuthorDto.builder()
                .id(author.getId())
                .newsCastId(author.getNewsCastAuthorId())
                .createdAt(author.getCreatedAt())
                .name(author.getName())
                .userName(author.getUserName())
                .messages(author.getMessages().stream().map(MessageConverter::toDto).collect(Collectors.toList()))
                .build();
    }

    public static Author toData(AuthorDto authorDto, Page page) {
        return Author.builder()
                .createdAt(authorDto.createdAt())
                .name(authorDto.name())
                .newsCastAuthorId(authorDto.newsCastId())
                .userName(authorDto.userName())
                .page(page)
                .build();
    }
}
