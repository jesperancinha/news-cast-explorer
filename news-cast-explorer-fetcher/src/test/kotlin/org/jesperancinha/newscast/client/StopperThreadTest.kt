package org.jesperancinha.newscast.client

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService

class StopperThreadTest {
    @Test
    fun testRun_whenRun_CallsServiceShutdown() {
        val executorServiceMock: ExecutorService = mockk()
        every { executorServiceMock.shutdownNow() } returns listOf()
        val stopperThread = StopperThread.builder().executorService(executorServiceMock).secondsDuration(1).build()
        stopperThread.start()
        stopperThread.join()
        verify { executorServiceMock.shutdownNow() }
    }
}