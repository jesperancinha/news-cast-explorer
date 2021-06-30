package org.jesperancinha.twitter.controller;

import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public List<MessageDto> getMessages() {
        return messageService.getMessages();
    }
}
