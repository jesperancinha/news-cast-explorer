package org.jesperancinha.newscast.converters;

import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.model.explorer.Message;

public class MessageConverter {
    public static MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getNewscastMessageId())
                .createdAt(message.getCreatedAt())
                .text(message.getText())
                .build();
    }

    public static Message toData(MessageDto messageDto) {
        return Message.builder()
                .createdAt(messageDto.getCreatedAt())
                .newscastMessageId(messageDto.getId())
                .text(messageDto.getText())
                .build();
    }
}
