package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.converters.MessageConverter;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> getMessages() {
        MessageRepository messageRepository = this.messageRepository;
        return messageRepository.findAll()
                .stream()
                .map(MessageConverter::toDto)
                .collect(Collectors.toList());
    }

}
