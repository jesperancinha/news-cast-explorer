package org.jesperancinha.twitter.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.twitter.hbc.httpclient.auth.Authentication
import io.kotest.matchers.nulls.shouldNotBeNull
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.List
import java.util.concurrent.BlockingQueue

@RunWith(MockitoJUnitRunner::class)
class TwitterClientImplJUnit4HamcrestTest {
    @Mock
    private val twitterMessageProcessor: TwitterMessageProcessor? = null

    @Mock
    private val authentication: Authentication? = null

    @Mock
    private val blockingQueue: BlockingQueue<String>? = null

    @Captor
    private val longArgumentCaptor: ArgumentCaptor<Long>? = null

    @Captor
    private val setArgumentCaptor: ArgumentCaptor<Set<String>>? = null
    private var twitterClient: TwitterClient? = null
    @Before
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
        val iterator: Iterator<String> = List.of("mockString").iterator()
        twitterClient!!.startFetchProcess()
        Mockito.verify(twitterMessageProcessor, Mockito.only())?.processAllMessages(
            setArgumentCaptor!!.capture(), longArgumentCaptor!!.capture(), longArgumentCaptor.capture())
        Mockito.verify(blockingQueue, Mockito.times(1))?.remainingCapacity()
        Mockito.verify(authentication, Mockito.atLeast(0))?.setupConnection(ArgumentMatchers.any())
        val allValues = longArgumentCaptor?.allValues
        allValues.shouldNotBeNull()
        MatcherAssert.assertThat(allValues, IsCollectionWithSize.hasSize(2))
        val startTimestamp = allValues[0]
        val endTimeStamp = allValues[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        MatcherAssert.assertThat(timeStampDiff, Matchers.greaterThanOrEqualTo(0L))
        MatcherAssert.assertThat(timeStampDiff, Matchers.lessThanOrEqualTo(1L))
        val value = setArgumentCaptor?.value
        MatcherAssert.assertThat(value, Matchers.`is`(Matchers.emptyCollectionOf(
            String::class.java)))
    }
}