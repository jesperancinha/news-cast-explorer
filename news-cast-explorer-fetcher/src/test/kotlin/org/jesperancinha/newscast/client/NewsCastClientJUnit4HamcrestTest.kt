package org.jesperancinha.newscast.client

import com.fasterxml.jackson.core.JsonProcessingException
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.collection.IsCollectionWithSize
import org.jesperancinha.newscast.processor.NewsCastMessageProcessor
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import java.util.concurrent.BlockingQueue

@ExtendWith(MockKExtension::class)
class NewsCastClientJUnit4HamcrestTest {
    @Mock
    lateinit var newsCastMessageProcessor: NewsCastMessageProcessor

    @Mock
    lateinit var blockingQueue: BlockingQueue<String>

    @Captor
    lateinit var longArgumentCaptor: ArgumentCaptor<Long>

    @Captor
    lateinit var setArgumentCaptor: ArgumentCaptor<Set<String>>

    lateinit var newsCastClient: NewsCastClient

    @Before
    fun setUp() {
        newsCastClient = NewsCastClient
            .builder()
            .newsCastMessageProcessor(newsCastMessageProcessor)
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
        newsCastClient.startFetchProcess()
        verify {
            newsCastMessageProcessor.processAllMessages(
                setArgumentCaptor.capture(), longArgumentCaptor.capture(), longArgumentCaptor.capture())
        }
        verify(exactly = 1) { blockingQueue.remainingCapacity() }
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