package org.jesperancinha.twitter.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.data.PageDto;

import java.util.Set;

public interface TwitterMessageProcessor {
    PageDto processAllMessages(Set<String> allMessages, Long timestampBefore, Long timestampAfter) throws JsonProcessingException;
}
