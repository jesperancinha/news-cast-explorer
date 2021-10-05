package org.jesperancinha.newscast.client

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService

class StopperThreadTest {
    @Test
    fun testRun_whenRun_CallsServiceShutdown() {
        val executorServiceMock: ExecutorService = mockk()
        val killerThread = StopperThread.builder().executorService(executorServiceMock).secondsDuration(1).build()
        killerThread.start()
        killerThread.join()
        verify { executorServiceMock.shutdownNow() }
    }
}