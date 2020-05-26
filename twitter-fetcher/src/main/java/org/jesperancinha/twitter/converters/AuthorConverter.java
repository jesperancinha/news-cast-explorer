package org.jesperancinha.twitter.converters;

import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.model.db.Author;
import org.jesperancinha.twitter.model.db.Page;

public class AuthorConverter {
    public static AuthorDto toDto(Author author) {
        return AuthorDto.builder()
                .id(author.getTwitterAuthorId())
                .createdAt(author.getCreatedAt())
                .name(author.getName())
                .nMessages(author.getNMessages())
                .screenName(author.getScreenName())
                .build();
    }

    public static Author toData(AuthorDto authorDto) {
        return Author.builder()
                .createdAt(authorDto.getCreatedAt())
                .name(authorDto.getName())
                .nMessages(authorDto.getNMessages())
                .twitterAuthorId(authorDto.getId())
                .screenName(authorDto.getScreenName())
                .build();
    }

    public static Author toData(AuthorDto authorDto, Page page) {
        return Author.builder()
                .createdAt(authorDto.getCreatedAt())
                .name(authorDto.getName())
                .nMessages(authorDto.getNMessages())
                .twitterAuthorId(authorDto.getId())
                .screenName(authorDto.getScreenName())
                .page(page)
                .build();
    }
}
