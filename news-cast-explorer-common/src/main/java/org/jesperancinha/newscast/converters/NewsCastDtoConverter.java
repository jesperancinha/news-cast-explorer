package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.model.source.Message;
import org.jesperancinha.newscast.model.source.User;

public class NewsCastDtoConverter {
    public static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .newsCastId(message.id())
                .text(message.text())
                .createdAt(message.createdAt().getTime())
                .build();
    }

    public static AuthorDto toUserDto(User user) {
        return AuthorDto.builder()
                .newsCastId(user.id())
                .name(user.name())
                .createdAt(user.createdAt().getTime())
                .screenName(user.screenName())
                .build();
    }
}
