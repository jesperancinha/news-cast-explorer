package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.model.twitter.Message;
import org.jesperancinha.newscast.model.twitter.User;

public class NewsCastDtoConverter {
    public static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.id())
                .text(message.text())
                .createdAt(message.createdAt().getTime())
                .build();
    }

    public static AuthorDto toUserDto(User user) {
        return AuthorDto.builder()
                .id(user.id())
                .name(user.name())
                .createdAt(user.createdAt().getTime())
                .screenName(user.screenName())
                .nMessages(0L)
                .build();
    }
}
