package org.jesperancinha.twitter.converters;

import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.model.twitter.Message;
import org.jesperancinha.twitter.model.twitter.User;

public class TwitterDtoConverter {
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
