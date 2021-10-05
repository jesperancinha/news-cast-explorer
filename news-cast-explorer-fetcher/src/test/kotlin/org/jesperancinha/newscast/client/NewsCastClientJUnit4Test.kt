package org.jesperancinha.newscast.client

import com.fasterxml.jackson.core.JsonProcessingException
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.newscast.processor.NewsCastMessageProcessor
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
class NewsCastClientJUnit4Test {
    @Mock
    lateinit var newsCastMessageProcessor: NewsCastMessageProcessor


    @Mock
    lateinit var blockingQueue: BlockingQueue<String>

    @Mock
    lateinit var searchTerms: List<String>

    @Captor
    lateinit var longArgumentCaptor: ArgumentCaptor<Long>

    @Captor
    lateinit var setArgumentCaptor: ArgumentCaptor<Set<String>>

    lateinit var newsCastClient: NewsCastClient

    @BeforeEach
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
        val iterator: Iterator<String> = java.util.List.of("mockString").iterator()
        Mockito.`when`(searchTerms.iterator()).thenReturn(iterator)
        newsCastClient.startFetchProcess()
        verify {
            newsCastMessageProcessor.processAllMessages(ArgumentMatchers.any(),
                longArgumentCaptor.capture(),
                longArgumentCaptor.capture())
        }
        verify(exactly = 1) { blockingQueue.remainingCapacity() }
        val allValues = longArgumentCaptor.allValues
        Assertions.assertThat(allValues).hasSize(2)
        allValues.shouldNotBeNull()
        val startTimestamp = allValues[0]
        val endTimeStamp = allValues[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        Assertions.assertThat(timeStampDiff).isBetween(0L, 1L)
        val value = setArgumentCaptor.value
        Assertions.assertThat(value).isEmpty()
    }
}