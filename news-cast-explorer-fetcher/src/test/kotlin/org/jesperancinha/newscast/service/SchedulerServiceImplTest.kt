package org.jesperancinha.newscast.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.newscast.client.NewsCastClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    properties = ["org.jesperancinha.newscast.host=http://localhost:8080"]
)
@ActiveProfiles("scheduler")
class SchedulerServiceImplTest(
    @Autowired
    val runningService: RunningService,
) {
    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @Test
    fun testStartProcess_whenRunnerCreated_thenSchedulerServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(SchedulerServiceImpl::class.java)
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenSchedulerServiceStart() {
        runningService.startProcess()
        verify { newsCastClient.startFetchProcess() }
    }

    @Test
    fun testStartProcess_whenSchedulerStarted_thenSchedulerServiceStarte() {
        (runningService as SchedulerServiceImpl?)!!.schedule()
        verify { newsCastClient.startFetchProcess() }
    }
}