package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.data.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getMessages();
}
