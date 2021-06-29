package org.jesperancinha.twitter.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.repository.AuthorRepository;
import org.jesperancinha.twitter.repository.MessageRepository;
import org.jesperancinha.twitter.repository.PageRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;

public class TwitterMessageProcessorImplExceptionJUnit4Test {

    private final TwitterMessageProcessor twitterMessageProcessor =
            TwitterMessageProcessorImpl
                    .builder()
                    .messageRepository(mock(MessageRepository.class))
                    .authorRepository(mock(AuthorRepository.class))
                    .pageRepository(mock(PageRepository.class))
                    .build();

    @Rule
    public ExpectedException exceptionCapturer = ExpectedException.none();

    @Test
    public void testMessages_whenMessageListInvalid_throwException() throws JsonProcessingException {
        var allMessages = Set.of("this is not a JSON", "And this is also not one!");

        exceptionCapturer.expectMessage(containsString("Expected BEGIN_OBJECT but was STRING at line 1 column 1 path"));

        twitterMessageProcessor
                .processAllMessages(allMessages, 1122333445566778899L, 998877665544332211L);
    }
}