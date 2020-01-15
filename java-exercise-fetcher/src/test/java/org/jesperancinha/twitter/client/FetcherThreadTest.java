package org.jesperancinha.twitter.client;

import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.httpclient.BasicClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testRun_whenFetchEmpty_thenReturnNoMessage() throws InterruptedException {
        MockitoAnnotations.initMocks(this);

        final List<String> allmessages = new ArrayList<>();

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

        verify(queueMock, only()).poll(1, TimeUnit.SECONDS);
        verify(clientMock, times(1)).connect();
        verify(clientMock, times(1)).stop();
        verify(clientMock, times(1)).isDone();
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