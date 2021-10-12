package org.jesperancinha.newscast.service;

import org.jesperancinha.newscast.converters.MessageConverter;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.model.explorer.Message;
import org.jesperancinha.newscast.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> getMessages() {
        MessageRepository messageRepository = this.messageRepository;
        return messageRepository.findAll()
                .stream()
                .map(MessageConverter::toDto)
                .collect(Collectors.toList());
    }

    public Optional<Message> findMessageById(Long idMessage) {
        return this.messageRepository.findById(idMessage);
    }
}
