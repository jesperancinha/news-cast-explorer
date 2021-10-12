package org.jesperancinha.newscast.client

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.jesperancinha.newscast.service.OneRunServiceImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

@SpringBootTest(properties = [
    "org.jesperancinha.newscast.host=http://localhost:8080",
    "org.jesperancinha.newscast.timeToWaitSeconds=5",
    "org.jesperancinha.newscast.capacity=5"
])
class FetcherThreadTest(
    @Autowired
    val fetcherThread: FetcherThread,
) {
    @MockkBean
    lateinit var queueMock: BlockingQueue<String>

    @MockkBean
    lateinit var executorService: ExecutorService

    @MockkBean(relaxed = true)
    lateinit var readerThread: ReaderThread

    @MockkBean(relaxed = true)
    lateinit var runningService: OneRunServiceImpl

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun testRun_whenFetchOk_thenReturnMessage() {
        every { queueMock.remainingCapacity() } returns 5
        every { queueMock.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
        every { executorService.execute(any()) } just runs
        every { executorService.shutdownNow() } returns listOf()
        fetcherThread.start()
        fetcherThread.join()
        val messages = fetcherThread.allMessages
        messages.shouldNotBeNull()
        messages.shouldHaveSize(1)
        messages.shouldContain("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { executorService.shutdownNow() }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun testRunWithCapacity5_whenFetchOk_thenReturnMessage() {
        every { queueMock.remainingCapacity() } returns 5
        every { queueMock.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
        every { executorService.shutdownNow() } returns listOf()
        every { executorService.execute(any()) } just runs
        fetcherThread.start()
        fetcherThread.join()
        val allMessages = fetcherThread.allMessages
        allMessages.shouldNotBeNull()
        allMessages.shouldHaveSize(1)
        allMessages.shouldContain("I am a message!")
        verify(exactly = 5) { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { executorService.shutdownNow() }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun testRunWithCapacity5_whenFetchOkButOneNull_thenReturnStill5Messages() {
        val testMessage = "I am a message!"
        val stringStack = Stack<String?>()
        stringStack.push(null)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        every { queueMock.remainingCapacity() } returns 5
        every { queueMock.poll(1, TimeUnit.SECONDS) } returnsMany (stringStack)
        every { executorService.shutdownNow() } returns listOf()
        every { executorService.execute(any()) } just runs
        fetcherThread.start()
        fetcherThread.join()
        val allMessages = fetcherThread.allMessages
        allMessages.shouldNotBeNull()
        allMessages.shouldHaveSize(1)
        allMessages.shouldContain("I am a message!")
        allMessages.shouldContainExactly("I am a message!")
        verify(atLeast = 5, atMost = 6) { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { executorService.shutdownNow() }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun testRun_whenFetchMostlyEmptyAndCapacity_thenReturnOneMessage() {
        val testMessage = "I am a message!"
        val stringStack = Stack<String?>()
        stringStack.push(null)
        stringStack.push(null)
        stringStack.push(null)
        stringStack.push(null)
        stringStack.push(testMessage)
        stringStack.push(null)
        stringStack.push(null)
        stringStack.push(null)
        every { queueMock.remainingCapacity() } returns 1
        every { queueMock.poll(1, TimeUnit.SECONDS) }.returnsMany(stringStack)
        every { executorService.execute(any()) } just runs
        every { executorService.shutdownNow() } returns listOf()
        fetcherThread.start()
        fetcherThread.join()
        val allMessages = fetcherThread.allMessages
        allMessages.shouldNotBeNull()
        allMessages.shouldHaveSize(1)
        allMessages.shouldContain("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { executorService.shutdownNow() }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    fun testRun_whenFetchMostlyTrackTimesamptAndCapacity_thenReturnOneMessage() {
        val testMessage = "I am a message!"
        val testLimitTrack = "{\"limit\":{\"track\":26,\"timestamp_ms\":\"1579144966748\"}}"
        val stringStack = Stack<String>()
        stringStack.push(null)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testMessage)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        every { queueMock.remainingCapacity() } returns 5
        every { queueMock.poll(1, TimeUnit.SECONDS) } returnsMany (stringStack)
        every { executorService.execute(any()) } just runs
        every { executorService.shutdownNow() } returns listOf()
        fetcherThread.start()
        fetcherThread.join()
        val allMessages = fetcherThread.allMessages
        allMessages.shouldNotBeNull()
        allMessages.shouldHaveSize(2)
        allMessages.shouldContain("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { executorService.shutdownNow() }
    }
}