package org.jesperancinha.newscast.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jesperancinha.newscast.config.BlockingQueueService
import org.jesperancinha.newscast.config.ExecutorServiceWrapper
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask
import java.util.concurrent.LinkedBlockingQueue

class StopperCallableTest {
    @Test
    fun testRun_whenRun_CallsServiceShutdown() {
        val executorServiceMock: ExecutorService = mockk()
        val executorServiceWrapper =
            ExecutorServiceWrapper(
                BlockingQueueService(100),
                0, 5, "http://localhost:8081/api/newscast/messages"
            )
        executorServiceWrapper.setExecutorService(executorServiceMock)
        every { executorServiceMock.shutdownNow() } returns listOf()
        val stopperCallable =
            StopperCallable.builder().executorServiceWrapper(executorServiceWrapper).secondsDuration(1).build()
        val thread = FutureTask(stopperCallable)
        thread.run()
        thread.get()
        verify { executorServiceMock.shutdownNow() }
    }
}