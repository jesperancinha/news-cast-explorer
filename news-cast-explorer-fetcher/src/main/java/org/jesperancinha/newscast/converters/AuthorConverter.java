package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.model.db.Author;
import org.jesperancinha.newscast.model.db.Page;

import java.util.stream.Collectors;

public class AuthorConverter {
    public static AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getNewsCastAuthorId())
                .createdAt(author.getCreatedAt())
                .name(author.getName())
                .nMessages(author.getNMessages())
                .screenName(author.getScreenName())
                .messageDtos(author.getMessages().stream().map(MessageConverter::toDto).collect(Collectors.toList()))
                .build();
    }

    public static Author toData(AuthorDto authorDto) {
        return Author.builder()
                .createdAt(authorDto.getCreatedAt())
                .name(authorDto.getName())
                .nMessages(authorDto.getNMessages())
                .newsCastAuthorId(authorDto.getId())
                .screenName(authorDto.getScreenName())
                .build();
    }

    public static Author toData(AuthorDto authorDto, Page page) {
        return Author.builder()
                .createdAt(authorDto.getCreatedAt())
                .name(authorDto.getName())
                .nMessages(authorDto.getNMessages())
                .newsCastAuthorId(authorDto.getId())
                .screenName(authorDto.getScreenName())
                .page(page)
                .build();
    }
}
