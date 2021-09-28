package org.jesperancinha.twitter.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.hbc.httpclient.auth.Authentication;
import org.jesperancinha.twitter.processor.TwitterMessageProcessor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterClientImplJUnit4Test {

    @Mock
    private TwitterMessageProcessor twitterMessageProcessor;

    @Mock
    private Authentication authentication;

    @Mock
    private BlockingQueue<String> blockingQueue;

    @Mock
    private List<String> searchTerms;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Set<String>> setArgumentCaptor;

    private TwitterClient twitterClient;

    @BeforeEach
    public void setUp() {
        this.twitterClient = TwitterClientImpl
                .builder()
                .twitterMessageProcessor(twitterMessageProcessor)
                .authentication(authentication)
                .searchTerm("test")
                .stringLinkedBlockingQueue(blockingQueue)
                .timeToWaitSeconds(0)
                .build();
    }

    /**
     * No exception is thrown while polling the buffer even though no connection has been made to twitter.
     *
     * @throws InterruptedException May occur while waiting for the executor to complete.
     */
    @Test
    public void testStartFetchProcess_whenProgrammed5Second_endsGracefully1Second() throws InterruptedException, JsonProcessingException {
        final Iterator<String> iterator = List.of("mockString").iterator();
        when(searchTerms.iterator()).thenReturn(iterator);

        twitterClient.startFetchProcess();

        verify(twitterMessageProcessor, only()).processAllMessages(any(), longArgumentCaptor.capture(), longArgumentCaptor.capture());
        verify(blockingQueue, times(1)).remainingCapacity();
        verify(authentication, atLeast(0)).setupConnection(any());
        final List<Long> allValues = longArgumentCaptor.getAllValues();
        assertThat(allValues).hasSize(2);
        final Long startTimestamp = allValues.get(0);
        final Long endTimeStamp = allValues.get(1);
        final long timeStampDiff = endTimeStamp - startTimestamp;
        assertThat(timeStampDiff).isBetween(0L, 1L);
        final Set<String> value = setArgumentCaptor.getValue();
        assertThat(value).isEmpty();

    }
}