package org.jesperancinha.newscast.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jesperancinha.newscast.config.ExecutorServiceWrapper
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue

class StopperThreadTest {
    @Test
    fun testRun_whenRun_CallsServiceShutdown() {
        val executorServiceMock: ExecutorService = mockk()
        val executorServiceWrapper =
            ExecutorServiceWrapper(LinkedBlockingQueue(100), 0, "http://localhost:8081/api/newscast/messages")
        executorServiceWrapper.setExecutorService(executorServiceMock)
        every { executorServiceMock.shutdownNow() } returns listOf()
        val stopperThread =
            StopperThread.builder().executorServiceWrapper(executorServiceWrapper).secondsDuration(1).build()
        stopperThread.start()
        stopperThread.join()
        verify { executorServiceMock.shutdownNow() }
    }
}