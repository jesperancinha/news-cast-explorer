package org.jesperancinha.twitter.client

import com.twitter.hbc.core.event.Event
import com.twitter.hbc.httpclient.BasicClient
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

@ExtendWith(MockKExtension::class)
class FetcherThreadTest {
    @MockK
    lateinit var queueMock: BlockingQueue<String>

    @MockK
    lateinit var executorServiceMock: ExecutorService

    @MockK(relaxed = true)
    lateinit var clientMock: BasicClient

    @Test
    fun testRun_whenFetchOk_thenReturnMessage() {
        val messages: Set<String> = HashSet()
        every { queueMock.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(1)
            .allMessages(messages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        Assertions.assertThat(messages).isNotNull
        Assertions.assertThat(messages).hasSize(1)
        Assertions.assertThat(messages).contains("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify(exactly = 1) { clientMock.connect() }
        verify(exactly = 1) { clientMock.stop() }
        verify(exactly = 1) { clientMock.isDone }
        verify { executorServiceMock.shutdownNow() }
    }

    @Test
    fun testRunWithCapacity5_whenFetchOk_thenReturnMessage() {
        val allmessages: Set<String> = HashSet()
        every { queueMock.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
        every { executorServiceMock.shutdownNow() } returns listOf()
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(5)
            .allMessages(allmessages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        allmessages.shouldNotBeNull()
        Assertions.assertThat(allmessages).hasSize(1)
        Assertions.assertThat(allmessages).contains("I am a message!")
        verify(exactly = 5) { queueMock.poll(1, TimeUnit.SECONDS) }
        verify(exactly = 1) { clientMock.connect() }
        verify(exactly = 1) { clientMock.stop() }
        verify(exactly = 5) { clientMock.isDone }
        verify { executorServiceMock.shutdownNow() }
    }

    @Test
    fun testRunWithCapacity5_whenFetchOkButOneNull_thenReturnStill5Messages() {
        val testMessage = "I am a message!"
        val allmessages: Set<String> = HashSet()
        val stringStack = Stack<String?>()
        stringStack.push(null)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        every { queueMock.poll(1, TimeUnit.SECONDS) } returnsMany (stringStack)
        every { executorServiceMock.shutdownNow() } returns listOf()
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(5)
            .allMessages(allmessages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        Assertions.assertThat(allmessages).isNotNull
        Assertions.assertThat(allmessages).hasSize(1)
        Assertions.assertThat(allmessages).contains("I am a message!")
        Assertions.assertThat(allmessages).containsOnly("I am a message!")
        verify(atLeast = 5, atMost = 6) { queueMock.poll(1, TimeUnit.SECONDS) }
        verify(exactly = 1) { clientMock.connect() }
        verify(exactly = 1) { clientMock.stop() }
        verify(atLeast = 5, atMost = 6) { clientMock.isDone }
        verify { executorServiceMock.shutdownNow() }
    }

    @Test
    fun testRun_whenFetchMostlyEmptyAndCapacity_thenReturnOneMessage() {
        val allmessages: Set<String> = HashSet()
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
        every { queueMock.poll(1, TimeUnit.SECONDS) }.returnsMany(stringStack)
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(1)
            .allMessages(allmessages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        Assertions.assertThat(allmessages).isNotNull
        Assertions.assertThat(allmessages).hasSize(1)
        Assertions.assertThat(allmessages).contains("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify(exactly = 1) { clientMock.connect() }
        verify(exactly = 1) { clientMock.stop() }
        verify { clientMock.isDone }
        verify { executorServiceMock.shutdownNow() }
    }

    @Test
    fun testRun_whenFetchMostlyTrackTimesamptAndCapacity_thenReturnOneMessage() {
        val allmessages: Set<String> = HashSet()
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
        every {
            queueMock.poll(1, TimeUnit.SECONDS)
        } returnsMany (stringStack)
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(1)
            .allMessages(allmessages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        Assertions.assertThat(allmessages).isNotNull
        Assertions.assertThat(allmessages).hasSize(1)
        Assertions.assertThat(allmessages).contains("I am a message!")
        verify { queueMock.poll(1, TimeUnit.SECONDS) }
        verify(exactly = 1) { clientMock.connect() }
        verify(exactly = 1) { clientMock.stop() }
        verify { clientMock.isDone }
        verify { executorServiceMock.shutdownNow() }
    }

    @Test
    fun testRun_whenClientIsDone_thenReturnNoMessage() {
        val allmessages: Set<String> = HashSet()
        val event: Event = mockk()
        every { clientMock.isDone } returns true
        every { clientMock.exitEvent } returns event
        val fetcherThread = FetcherThread.builder()
            .stringLinkedBlockingQueue(queueMock)
            .capacity(1)
            .allMessages(allmessages)
            .executorService(executorServiceMock)
            .client(clientMock)
            .build()
        fetcherThread.start()
        fetcherThread.join()
        allmessages.shouldNotBeNull()
        allmessages.shouldBeEmpty()
        verify(exactly = 0) { queueMock.poll(1, TimeUnit.SECONDS) }
        verify { clientMock.connect() }
        verify { clientMock.isDone }
        verify { clientMock.exitEvent }
    }
}