package org.jesperancinha.twitter.client

import com.fasterxml.jackson.core.JsonProcessingException
import com.twitter.hbc.httpclient.auth.Authentication
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import java.util.concurrent.BlockingQueue

@ExtendWith(MockKExtension::class)
class TwitterClientImplJUnit4HamcrestTest {
    @Mock
    lateinit var twitterMessageProcessor: TwitterMessageProcessor

    @Mock
    lateinit var authentication: Authentication

    @Mock
    lateinit var blockingQueue: BlockingQueue<String>

    @Captor
    lateinit var longArgumentCaptor: ArgumentCaptor<Long>

    @Captor
    lateinit var setArgumentCaptor: ArgumentCaptor<Set<String>>

    lateinit var twitterClient: TwitterClient

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
        val iterator: Iterator<String> = listOf("mockString").iterator()
        twitterClient.startFetchProcess()
        verify {
            twitterMessageProcessor.processAllMessages(
                setArgumentCaptor.capture(), longArgumentCaptor.capture(), longArgumentCaptor.capture())
        }
        verify(exactly = 1) { blockingQueue.remainingCapacity() }
        Mockito.verify(authentication, Mockito.atLeast(0))?.setupConnection(ArgumentMatchers.any())
        val allValues = longArgumentCaptor.allValues
        allValues.shouldNotBeNull()
        MatcherAssert.assertThat(allValues, IsCollectionWithSize.hasSize(2))
        val startTimestamp = allValues[0]
        val endTimeStamp = allValues[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        MatcherAssert.assertThat(timeStampDiff, Matchers.greaterThanOrEqualTo(0L))
        MatcherAssert.assertThat(timeStampDiff, Matchers.lessThanOrEqualTo(1L))
        val value = setArgumentCaptor.value
        MatcherAssert.assertThat(value, Matchers.`is`(Matchers.emptyCollectionOf(
            String::class.java)))
    }
}