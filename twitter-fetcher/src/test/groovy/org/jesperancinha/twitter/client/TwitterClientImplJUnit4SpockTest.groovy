package org.jesperancinha.twitter.client

import com.twitter.hbc.httpclient.auth.Authentication
import org.assertj.core.api.SoftAssertions
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.mockito.ArgumentCaptor
import spock.lang.Specification

import java.util.concurrent.BlockingQueue

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*

class TwitterClientImplJUnit4SpockTest extends Specification {

    private TwitterMessageProcessor twitterMessageProcessor = mock(TwitterMessageProcessor)

    private Authentication authentication = mock(Authentication.class)

    private BlockingQueue<String> blockingQueue = mock(BlockingQueue.class)

    private ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class)

    private TwitterClient twitterClient

    def setup() {
        this.twitterClient = TwitterClientImpl
                .builder()
                .twitterMessageProcessor(twitterMessageProcessor)
                .authentication(authentication)
                .searchTerm("test")
                .stringLinkedBlockingQueue(blockingQueue)
                .timeToWaitSeconds(0)
                .build()
    }

    def cleanup() {
    }

    def "Should end gracefully when timeout is 0"() {
        given:
        def twitterClient = TwitterClientImpl
                .builder()
                .host("http://dummy.twitter.stream")
                .authentication(authentication)
                .twitterMessageProcessor(twitterMessageProcessor)
                .stringLinkedBlockingQueue(blockingQueue)
                .searchTerm("test")
                .timeToWaitSeconds(0)
                .build()
        when:
        twitterClient.startFetchProcess()

        then:
        SoftAssertions.assertSoftly { softly ->
            verify(twitterMessageProcessor, only()).processAllMessages(any(), longArgumentCaptor.capture(), longArgumentCaptor.capture())
            verify(blockingQueue, times(1)).remainingCapacity()
            verify(authentication, atLeast(0)).setupConnection(any())
            final List<Long> allValues = longArgumentCaptor.getAllValues()
            assertThat(allValues).hasSize(2)
            final Long startTimestamp = allValues.get(0)
            final Long endTimeStamp = allValues.get(1)
            final long timeStampDiff = endTimeStamp - startTimestamp
            assertThat(timeStampDiff).isBetween(0L, 10L)
        }
    }
}
