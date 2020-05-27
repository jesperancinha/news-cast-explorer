package org.jesperancinha.twitter.client;

import com.twitter.hbc.httpclient.auth.Authentication;
import org.jesperancinha.twitter.processor.TwitterMessageProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterClientImplJUnit4Test {

    @InjectMocks
    private TwitterClientImpl twitterClient;

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

    /**
     * No exception is thrown while polling the buffer even though no connection has been made to twitter.
     *
     * @throws InterruptedException May occur while waiting for the executor to complete.
     */
    @Test
    public void testStartFetchProcess_whenProgrammed5Second_endsGracefully1Second() throws InterruptedException {
        final Iterator<String> iterator = List.of("mockString").iterator();
        when(searchTerms.iterator()).thenReturn(iterator);

        twitterClient.startFetchProcess();

        verify(twitterMessageProcessor, only()).processAllMessages(any(), longArgumentCaptor.capture(), longArgumentCaptor.capture());
        verify(blockingQueue, times(1)).remainingCapacity();
        verify(authentication,  atLeast(0)).setupConnection(any());
        final List<Long> allValues = longArgumentCaptor.getAllValues();
        assertThat(allValues).hasSize(2);
        final Long startTimestamp = allValues.get(0);
        final Long endTimeStamp = allValues.get(1);
        final long timeStampDiff = endTimeStamp - startTimestamp;
        assertThat(timeStampDiff).isGreaterThanOrEqualTo(0);
    }
}