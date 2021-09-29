package org.jesperancinha.twitter.converters;

import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.model.db.Author;
import org.jesperancinha.twitter.model.db.Message;

public class MessageConverter {
    public static MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getTwitterMessageId())
                .createdAt(message.getCreatedAt())
                .text(message.getText())
                .build();
    }

    public static Message toData(MessageDto messageDto, Author author) {
        return Message.builder()
                .createdAt(messageDto.getCreatedAt())
                .twitterMessageId(messageDto.getId())
                .text(messageDto.getText())
//                .author(author)
                .build();
    }
}
