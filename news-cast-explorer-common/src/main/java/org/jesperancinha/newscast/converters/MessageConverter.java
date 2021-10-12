package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.model.explorer.Message;

public class MessageConverter {
    public static MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .newsCastId(message.getNewscastMessageId())
                .createdAt(message.getCreatedAt())
                .text(message.getText())
                .build();
    }

    public static Message toData(MessageDto messageDto, Author author) {
        return Message.builder()
                .createdAt(messageDto.createdAt())
                .author(author)
                .newscastMessageId(messageDto.newsCastId())
                .text(messageDto.text())
                .build();
    }
}
