package org.jesperancinha.twitter.client

import com.twitter.hbc.httpclient.auth.Authentication
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.processor.TwitterMessageProcessor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import java.util.concurrent.BlockingQueue

@ExtendWith(MockKExtension::class)
internal class TwitterClientImplJUnit5Test {
    @MockK(relaxed = true)
    lateinit var twitterMessageProcessor: TwitterMessageProcessor

    @MockK(relaxed = true)
    lateinit var authentication: Authentication

    @MockK(relaxed = true)
    lateinit var blockingQueue: BlockingQueue<String>

    lateinit var twitterClient: TwitterClient

    @BeforeEach
    fun setUp() {
        twitterClient = TwitterClientImpl
            .builder()
            .host("http://dummy.twitter.stream")
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
    fun testStartFetchProcess_whenProgrammed5Second_endsGracefullyImmediatelly() {
        twitterClient.startFetchProcess()
        val longArgumentCaptor = mutableListOf<Long>()
        verify {
            twitterMessageProcessor.processAllMessages(any(),
                capture(longArgumentCaptor),
                capture(longArgumentCaptor))
        }
        verify(exactly = 1) { blockingQueue.remainingCapacity() }
        verify { authentication.setupConnection(any()) }
        Assertions.assertThat(longArgumentCaptor).hasSize(2)
        longArgumentCaptor.shouldNotBeNull()
        val startTimestamp = longArgumentCaptor[0]
        val endTimeStamp = longArgumentCaptor[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        Assertions.assertThat(timeStampDiff).isGreaterThanOrEqualTo(0)
        Assertions.assertThat(timeStampDiff).isLessThan(1)
    }
}