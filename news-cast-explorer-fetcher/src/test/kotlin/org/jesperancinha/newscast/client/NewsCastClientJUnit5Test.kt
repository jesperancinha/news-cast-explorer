package org.jesperancinha.newscast.client

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeBetween
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jesperancinha.newscast.config.BlockingQueueService
import org.jesperancinha.newscast.processor.NewsCastMessageProcessor
import org.jesperancinha.newscast.service.OneRunServiceImpl
import org.jesperancinha.newscast.utils.AbstractNCTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS
import org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD
import org.springframework.test.annotation.DirtiesContext.MethodMode.BEFORE_METHOD
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

@SpringBootTest(
    properties = [
        "org.jesperancinha.newscast.host=http://localhost:8080",
        "org.jesperancinha.newscast.timeToWaitSeconds=5"
    ]
)
@DirtiesContext(classMode = BEFORE_CLASS, methodMode = BEFORE_METHOD)
@ActiveProfiles("non-scheduler")
internal class NewsCastClientJUnit5Test @Autowired constructor(
    val newsCastClient: NewsCastClient
) : AbstractNCTest() {
    @MockkBean(relaxed = true)
    lateinit var newsCastMessageProcessor: NewsCastMessageProcessor

    @MockkBean(relaxed = true)
    lateinit var blockingQueueService: BlockingQueueService

    @MockkBean(relaxed = true)
    lateinit var runningService: OneRunServiceImpl

    /**
     * No exception is thrown while polling the buffer even though no connection has been made to newscast.
     * Temporarily disabled because it does not seem to work in GitHub Actions Pipeline
     *
     * @throws InterruptedException May occur while waiting for the executor to complete.
     */
    @Test
    fun `should end gracefully after 5 seconds immediately with 1 s tolerance`() {
        val linkedBlockingQueue = mockk<BlockingQueue<String>>()
        every { blockingQueueService.blockingQueue } returns linkedBlockingQueue
        newsCastClient.startFetchProcess()
        newsCastClient.startFetchProcess()
        val longArgumentCaptor = mutableListOf<Long>()
        verify {
            newsCastMessageProcessor.processAllMessages(
                any(),
                capture(longArgumentCaptor),
                capture(longArgumentCaptor)
            )
        }
        verify(exactly = 2) { linkedBlockingQueue.remainingCapacity() }
        verify(exactly = 1) { runningService.startProcess() }
        longArgumentCaptor.shouldHaveSize(4)
        longArgumentCaptor.shouldNotBeNull()
        val startTimestamp = longArgumentCaptor[0]
        val endTimeStamp = longArgumentCaptor[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        timeStampDiff.shouldBeGreaterThanOrEqual(0)
        timeStampDiff.shouldBeBetween(4, 6)
    }
}