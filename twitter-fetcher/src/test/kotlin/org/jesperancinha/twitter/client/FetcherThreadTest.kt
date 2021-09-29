package org.jesperancinha.twitter.client

import com.twitter.hbc.core.event.Event
import com.twitter.hbc.httpclient.BasicClient
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
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

    @MockK
    lateinit var clientMock: BasicClient

    @Test
    @Throws(InterruptedException::class)
    fun testRun_whenFetchOk_thenReturnMessage() {
        MockitoAnnotations.initMocks(this)
        val allmessages: Set<String> = HashSet()
        every { queueMock?.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
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
        Mockito.verify(queueMock, Mockito.only())?.poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1))?.connect()
        Mockito.verify(clientMock, Mockito.times(1))?.stop()
        Mockito.verify(clientMock, Mockito.times(1))?.isDone
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRunWithCapacity5_whenFetchOk_thenReturnMessage() {
        MockitoAnnotations.initMocks(this)
        val allmessages: Set<String> = HashSet()
        every { queueMock!!.poll(1, TimeUnit.SECONDS) } returns "I am a message!"
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
        Mockito.verify(queueMock, Mockito.times(5))?.poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1))?.connect()
        Mockito.verify(clientMock, Mockito.times(1))?.stop()
        Mockito.verify(clientMock, Mockito.times(5))?.isDone
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRunWithCapacity5_whenFetchOkButOneNull_thenReturnStill5Messages() {
        MockitoAnnotations.initMocks(this)
        val testMessage = "I am a message!"
        val allmessages: Set<String> = HashSet()
        val stringStack = Stack<String?>()
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(testMessage)
        stringStack.push(null)
        Mockito.`when`(queueMock!!.poll(1, TimeUnit.SECONDS))
            .thenAnswer { invocationOnMock: InvocationOnMock? -> stringStack.pop() }
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
        Mockito.verify(queueMock, Mockito.times(6)).poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1))?.connect()
        Mockito.verify(clientMock, Mockito.times(1))?.stop()
        Mockito.verify(clientMock, Mockito.times(6))?.isDone
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRun_whenFetchMostlyEmptyAndCapacity_thenReturnOneMessage() {
        MockitoAnnotations.initMocks(this)
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
        Mockito.`when`(queueMock!!.poll(1, TimeUnit.SECONDS))
            .thenAnswer { invocationOnMock: InvocationOnMock? -> stringStack.pop() }
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
        Mockito.verify(queueMock, Mockito.atLeastOnce()).poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1))?.connect()
        Mockito.verify(clientMock, Mockito.times(1))?.stop()
        Mockito.verify(clientMock, Mockito.atLeastOnce())?.isDone
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRun_whenFetchMostlyTrackTimesamptAndCapacity_thenReturnOneMessage() {
        MockitoAnnotations.initMocks(this)
        val allmessages: Set<String> = HashSet()
        val testMessage = "I am a message!"
        val testLimitTrack = "{\"limit\":{\"track\":26,\"timestamp_ms\":\"1579144966748\"}}"
        val stringStack = Stack<String>()
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testMessage)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        stringStack.push(testLimitTrack)
        Mockito.`when`(queueMock!!.poll(1, TimeUnit.SECONDS))
            .thenAnswer { invocationOnMock: InvocationOnMock? -> stringStack.pop() }
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
        Mockito.verify(queueMock, Mockito.atLeastOnce()).poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1))?.connect()
        Mockito.verify(clientMock, Mockito.times(1))?.stop()
        Mockito.verify(clientMock, Mockito.atLeastOnce())?.isDone
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testRun_whenClientIsDone_thenReturnNoMessage() {
        MockitoAnnotations.initMocks(this)
        val allmessages: Set<String> = HashSet()
        val event = Mockito.mock(Event::class.java)
        Mockito.`when`(clientMock!!.isDone).thenReturn(true)
        Mockito.`when`(clientMock.exitEvent).thenReturn(event)
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
        Assertions.assertThat(allmessages).isEmpty()
        Mockito.verify(queueMock, Mockito.never())?.poll(1, TimeUnit.SECONDS)
        Mockito.verify(clientMock, Mockito.times(1)).connect()
        Mockito.verify(clientMock, Mockito.times(1)).isDone
        Mockito.verify(clientMock, Mockito.times(1)).exitEvent
        Mockito.verify(clientMock, Mockito.times(1)).stop()
        Mockito.verify(executorServiceMock, Mockito.only())?.shutdownNow()
    }
}