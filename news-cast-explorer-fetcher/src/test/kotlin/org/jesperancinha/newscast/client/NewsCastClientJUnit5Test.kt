package org.jesperancinha.newscast.client

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.verify
import org.jesperancinha.newscast.processor.NewsCastMessageProcessor
import org.jesperancinha.newscast.service.OneRunServiceImpl
import org.jesperancinha.newscast.utils.AbstractNCTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.util.concurrent.BlockingQueue

@SpringBootTest(
    properties = [
        "org.jesperancinha.newscast.host=http://localhost:8080",
        "org.jesperancinha.newscast.timeToWaitSeconds=5"
    ]
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("non-scheduler")
internal class NewsCastClientJUnit5Test @Autowired constructor(
    val newsCastClient: NewsCastClient
) : AbstractNCTest() {
    @MockkBean(relaxed = true)
    lateinit var newsCastMessageProcessor: NewsCastMessageProcessor

    @MockkBean(relaxed = true)
    lateinit var blockingQueue: BlockingQueue<String>

    @MockkBean(relaxed = true)
    lateinit var runningService: OneRunServiceImpl

    /**
     * No exception is thrown while polling the buffer even though no connection has been made to newscast.
     *
     * @throws InterruptedException May occur while waiting for the executor to complete.
     */
    @Test
    fun testStartFetchProcess_whenProgrammed5Second_endsGracefullyImmediately() {
        newsCastClient.startFetchProcess()
        val longArgumentCaptor = mutableListOf<Long>()
        verify {
            newsCastMessageProcessor.processAllMessages(
                any(),
                capture(longArgumentCaptor),
                capture(longArgumentCaptor)
            )
        }
        verify(exactly = 1) { blockingQueue.remainingCapacity() }
        verify(exactly = 1) { runningService.startProcess() }
        longArgumentCaptor.shouldHaveSize(2)
        longArgumentCaptor.shouldNotBeNull()
        val startTimestamp = longArgumentCaptor[0]
        val endTimeStamp = longArgumentCaptor[1]
        val timeStampDiff = endTimeStamp - startTimestamp
        timeStampDiff.shouldBeGreaterThanOrEqual(0)
        timeStampDiff.shouldBeLessThanOrEqual(5)
    }
}