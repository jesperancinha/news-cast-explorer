package org.jesperancinha.twitter.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.twitter.hbc.httpclient.auth.Authentication
import io.kotest.matchers.nulls.shouldNotBeNull
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.concurrent.BlockingQueue

@ExtendWith(MockitoExtension::class)
internal class TwitterClientImplJUnit5Test {
    @Mock
    private val twitterMessageProcessor: TwitterMessageProcessor? = null

    @Mock
    private val authentication: Authentication? = null

    @Mock
    private val blockingQueue: BlockingQueue<String>? = null

    @Mock
    private val searchTerms: List<String>? = null

    @Captor
    private val longArgumentCaptor: ArgumentCaptor<Long>? = null
    private var twitterClient: TwitterClient? = null
    @BeforeEach
    fun setUp() {
        twitterClient = TwitterClientImpl
            .builder()
            .twitterMessageProcessor(twitterMessageProcessor)
            .authentication(authentication)
            .searchTerm("test")
            .stringLinkedBlockingQueue(blockingQueue)
            .timeToWaitSeconds(0)
            .build()
    }

    /**
     * No exception is thrown while polling the buffer even though no connection has been made to twitter.
     *
     * @throws InterruptedException May occur while waiting for the executor to complete.
     */
    @Test
    @Throws(InterruptedException::class, JsonProcessingException::class)
    fun testStartFetchProcess_whenProgrammed5Second_endsGracefullyImmediatelly() {
        twitterClient!!.startFetchProcess()
        Mockito.verify(twitterMessageProcessor, Mockito.only())
            ?.processAllMessages(ArgumentMatchers.any(), longArgumentCaptor!!.capture(), longArgumentCaptor.capture())
        Mockito.verify(blockingQueue, Mockito.times(1))?.remainingCapacity()
        Mockito.verify(authentication, Mockito.atLeast(0))?.setupConnection(ArgumentMatchers.any())
        val allValues = longArgumentCaptor?.allValues
        Assertions.assertThat(allValues).hasSize(2)
        allValues.shouldNotBeNull()
        val startTimestamp = allValues[0]
        val endTimeStamp = allValues[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        Assertions.assertThat(timeStampDiff).isGreaterThanOrEqualTo(0)
        Assertions.assertThat(timeStampDiff).isLessThan(1)
    }
}