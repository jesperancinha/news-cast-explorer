package org.jesperancinha.newscast.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jesperancinha.newscast.config.ExecutorServiceWrapper
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService

class StopperThreadTest {
    @Test
    fun testRun_whenRun_CallsServiceShutdown() {
        val executorServiceMock: ExecutorService = mockk()
        val executorServiceWrapper = ExecutorServiceWrapper(executorServiceMock)
        every { executorServiceMock.shutdownNow() } returns listOf()
        val stopperThread =
            StopperThread.builder().executorServiceWrapper(executorServiceWrapper).secondsDuration(1).build()
        stopperThread.start()
        stopperThread.join()
        verify { executorServiceMock.shutdownNow() }
    }
}