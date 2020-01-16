package org.jesperancinha.twitter.client;

import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.httpclient.BasicClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetcherThreadTest {

    @Mock
    private BlockingQueue<String> queueMock;

    @Mock
    private ExecutorService executorServiceMock;

    @Mock
    private BasicClient clientMock;

    @Test
    public void testRun_whenFetchOk_thenReturnMessage() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final List<String> allmessages = new ArrayList<>();

        when(queueMock.poll(1, TimeUnit.SECONDS)).thenReturn("I am a message!");

        final FetcherThread fetcherThread = FetcherThread.builder()
                .stringLinkedBlockingQueue(queueMock)
                .capacity(1)
                .allMessages(allmessages)
                .executorService(executorServiceMock)
                .client(clientMock)
                .build();

        fetcherThread.start();

        fetcherThread.join();

        assertThat(allmessages).isNotNull();
        assertThat(allmessages).hasSize(1);
        assertThat(allmessages).contains("I am a message!");

        verify(queueMock, only()).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).stop();
        verify(clientMock, times(1)).isDone();
        verify(executorServiceMock, only()).shutdownNow();

    }

    @Test
    public void testRunWithCapacity5_whenFetchOk_thenReturnMessage() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final List<String> allmessages = new ArrayList<>();

        when(queueMock.poll(1, TimeUnit.SECONDS)).thenReturn("I am a message!");

        final FetcherThread fetcherThread = FetcherThread.builder()
                .stringLinkedBlockingQueue(queueMock)
                .capacity(5)
                .allMessages(allmessages)
                .executorService(executorServiceMock)
                .client(clientMock)
                .build();

        fetcherThread.start();

        fetcherThread.join();

        assertThat(allmessages).isNotNull();
        assertThat(allmessages).hasSize(5);
        assertThat(allmessages).contains("I am a message!");

        verify(queueMock, times(5)).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).stop();
        verify(clientMock, times(5)).isDone();
        verify(executorServiceMock, only()).shutdownNow();

    }


    @Test
    public void testRunWithCapacity5_whenFetchOkButOneNull_thenReturnStill5Messages() throws InterruptedException {
        MockitoAnnotations.initMocks(this);
        final String testMessage = "I am a message!";
        final List<String> allmessages = new ArrayList<>();

        final Stack<String> stringStack = new Stack<>();
        stringStack.push(testMessage);
        stringStack.push(testMessage);
        stringStack.push(testMessage);
        stringStack.push(testMessage);
        stringStack.push(testMessage);
        stringStack.push(null);
        when(queueMock.poll(1, TimeUnit.SECONDS)).thenAnswer(invocationOnMock -> stringStack.pop());

        final FetcherThread fetcherThread = FetcherThread.builder()
                .stringLinkedBlockingQueue(queueMock)
                .capacity(5)
                .allMessages(allmessages)
                .executorService(executorServiceMock)
                .client(clientMock)
                .build();

        fetcherThread.start();

        fetcherThread.join();

        assertThat(allmessages).isNotNull();
        assertThat(allmessages).hasSize(5);
        assertThat(allmessages).contains("I am a message!");
        assertThat(allmessages).containsOnly("I am a message!");

        verify(queueMock, times(6)).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).stop();
        verify(clientMock, times(6)).isDone();
        verify(executorServiceMock, only()).shutdownNow();

    }

    @Test
    public void testRun_whenFetchMostlyEmptyAndCapacity_thenReturnOneMessage() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final List<String> allmessages = new ArrayList<>();
        final String testMessage = "I am a message!";

        final Stack<String> stringStack = new Stack<>();
        stringStack.push(null);
        stringStack.push(null);
        stringStack.push(null);
        stringStack.push(null);
        stringStack.push(testMessage);
        stringStack.push(null);
        stringStack.push(null);
        stringStack.push(null);

        when(queueMock.poll(1, TimeUnit.SECONDS)).thenAnswer(invocationOnMock -> stringStack.pop());

        final FetcherThread fetcherThread = FetcherThread.builder()
                .stringLinkedBlockingQueue(queueMock)
                .capacity(1)
                .allMessages(allmessages)
                .executorService(executorServiceMock)
                .client(clientMock)
                .build();

        fetcherThread.start();

        fetcherThread.join();

        assertThat(allmessages).isNotNull();
        assertThat(allmessages).hasSize(1);
        assertThat(allmessages).contains("I am a message!");

        verify(queueMock, atLeastOnce()).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).stop();
        verify(clientMock, atLeastOnce()).isDone();
        verify(executorServiceMock, only()).shutdownNow();

    }

    @Test
    public void testRun_whenClientIsDone_thenReturnNoMessage() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final List<String> allmessages = new ArrayList<>();
        final Event event = mock(Event.class);
        when(clientMock.isDone()).thenReturn(true);
        when(clientMock.getExitEvent()).thenReturn(event);

        final FetcherThread fetcherThread = FetcherThread.builder()
                .stringLinkedBlockingQueue(queueMock)
                .capacity(1)
                .allMessages(allmessages)
                .executorService(executorServiceMock)
                .client(clientMock)
                .build();

        fetcherThread.start();

        fetcherThread.join();

        assertThat(allmessages).isNotNull();
        assertThat(allmessages).isEmpty();

        verify(queueMock, never()).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).isDone();
        verify(clientMock, times(1)).getExitEvent();
        verify(clientMock, times(1)).stop();
        verify(executorServiceMock, only()).shutdownNow();

    }
}