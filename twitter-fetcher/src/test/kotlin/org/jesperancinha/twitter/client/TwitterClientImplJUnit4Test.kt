package org.jesperancinha.twitter.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.twitter.hbc.httpclient.auth.Authentication
import io.kotest.matchers.nulls.shouldNotBeNull
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.BlockingQueue

@RunWith(MockitoJUnitRunner::class)
class TwitterClientImplJUnit4Test {
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

    @Captor
    private val setArgumentCaptor: ArgumentCaptor<Set<String>>? = null
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
    fun testStartFetchProcess_whenProgrammed5Second_endsGracefully1Second() {
        val iterator: Iterator<String> = java.util.List.of("mockString").iterator()
        Mockito.`when`(searchTerms!!.iterator()).thenReturn(iterator)
        twitterClient!!.startFetchProcess()
        Mockito.verify(twitterMessageProcessor, Mockito.only())?.processAllMessages(ArgumentMatchers.any(), longArgumentCaptor!!.capture(), longArgumentCaptor.capture())
        Mockito.verify(blockingQueue, Mockito.times(1))?.remainingCapacity()
        Mockito.verify(authentication, Mockito.atLeast(0))?.setupConnection(ArgumentMatchers.any())
        val allValues = longArgumentCaptor?.allValues
        Assertions.assertThat(allValues).hasSize(2)
        allValues.shouldNotBeNull()
        val startTimestamp = allValues[0]
        val endTimeStamp = allValues[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        Assertions.assertThat(timeStampDiff).isBetween(0L, 1L)
        val value = setArgumentCaptor!!.value
        Assertions.assertThat(value).isEmpty()
    }
}