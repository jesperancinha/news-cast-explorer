package org.jesperancinha.newscast.service

import com.ninjasquad.springmockk.MockkBean
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.verify
import org.jesperancinha.newscast.client.NewsCastClient
import org.jesperancinha.newscast.utils.AbstractNCTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = ["org.jesperancinha.newscast.host=http://localhost:8080"]
)
class OneRunServiceImplTest(
    @Autowired
    val runningService: RunningService,
) : AbstractNCTest() {
    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @Test
    fun testStartProcess_whenRunnerCreated_thenOneServiceInstance() {
        runningService.shouldBeInstanceOf<OneRunServiceImpl>()
        verify { newsCastClient.startFetchProcess() }
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenOneServiceStart() {
        runningService.startProcess()
        verify { newsCastClient.startFetchProcess() }
    }
}