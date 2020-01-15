package org.jesperancinha.twitter.client;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

public class KillerThreadTest {

    @Test
    void testRun_whenRun_CallsServiceShutdown() throws InterruptedException {
        final ExecutorService executorServiceMock = mock(ExecutorService.class);
        final KillerThread killerThread = KillerThread.builder().executorService(executorServiceMock).secondsDuration(1).build();

        killerThread.start();

        killerThread.join();

        verify(executorServiceMock, only()).shutdownNow();
    }
}