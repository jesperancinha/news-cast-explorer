package org.jesperancinha.twitter.client;

import org.jesperancinha.twitter.processor.TwitterMessageProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TwitterClientTest {

    @Mock
    private TwitterMessageProcessor processor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Test
    void testStartFetchProcess_whenProgrammed1Second_endsGracefully0Seconds() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final int capacity = 1;
        final String consumerKey = "consumerKey";
        final String consumerSecret = "consumerSecret";
        final String token = "token";
        final String tokenSecret = "tokenSecret";
        final String searchTerm = "rogerfederer";
        final int timeToWait = 5;
        final TwitterClient twitterClient = TwitterClient.builder()
                .twitterMessageProcessor(processor)
                .capacity(capacity)
                .consumerKey(consumerKey)
                .consumerSecret(consumerSecret)
                .token(token)
                .tokenSecret(tokenSecret)
                .searchTerm(searchTerm)
                .timeToWaitSeconds(timeToWait)
                .build();

        twitterClient.startFetchProcess();

        verify(processor, only()).processAllMessages(any(), longArgumentCaptor.capture(), longArgumentCaptor.capture());
        final List<Long> allValues = longArgumentCaptor.getAllValues();
        assertThat(allValues).hasSize(2);
        final Long startTimestamp = allValues.get(0);
        final Long endTimeStamp = allValues.get(1);
        final long timeStampDiff = endTimeStamp - startTimestamp;
        assertThat(timeStampDiff).isZero();
    }
}