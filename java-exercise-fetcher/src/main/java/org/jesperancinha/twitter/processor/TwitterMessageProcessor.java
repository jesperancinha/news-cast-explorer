package org.jesperancinha.twitter.processor;

import org.jesperancinha.twitter.data.PageDto;

import java.util.List;

public interface TwitterMessageProcessor {
    PageDto processAllMessages(List<String> allMessages, Long timestampBefore, Long timestampAfter);
}
