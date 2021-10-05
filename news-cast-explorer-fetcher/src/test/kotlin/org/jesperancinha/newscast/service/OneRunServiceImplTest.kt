package org.jesperancinha.newscast.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.newscast.client.NewsCastClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = ["org.jesperancinha.newscast.host=http://localhost:8080"]
)
class OneRunServiceImplTest(
    @Autowired
    val runningService: RunningService,
) {
    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @Test
    fun testStartProcess_whenRunnerCreated_thenOneServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(OneRunServiceImpl::class.java)
        verify { newsCastClient.startFetchProcess() }
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenOneServiceStart() {
        runningService.startProcess()
        verify { newsCastClient.startFetchProcess() }
    }
}