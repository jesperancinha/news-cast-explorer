package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.model.explorer.Page;

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
                .createdAt(authorDto.createdAt())
                .name(authorDto.name())
                .nMessages(authorDto.nMessages())
                .newsCastAuthorId(authorDto.id())
                .screenName(authorDto.screenName())
                .build();
    }

    public static Author toData(AuthorDto authorDto, Page page) {
        return Author.builder()
                .createdAt(authorDto.createdAt())
                .name(authorDto.name())
                .nMessages(authorDto.nMessages())
                .newsCastAuthorId(authorDto.id())
                .screenName(authorDto.screenName())
                .page(page)
                .build();
    }
}
