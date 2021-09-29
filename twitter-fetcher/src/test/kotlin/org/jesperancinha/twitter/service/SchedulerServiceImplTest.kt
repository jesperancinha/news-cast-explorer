package org.jesperancinha.twitter.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("scheduler")
class SchedulerServiceImplTest(
    @Autowired
    val runningService: RunningService,
) {
    @MockkBean(relaxed = true)
    lateinit var twitterClient: TwitterClient

    @Test
    fun testStartProcess_whenRunnerCreated_thenSchedulerServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(SchedulerServiceImpl::class.java)
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenSchedulerServiceStart() {
        runningService.startProcess()
        verify { twitterClient.startFetchProcess() }
    }

    @Test
    fun testStartProcess_whenSchedulerStarted_thenSchedulerServiceStarte() {
        (runningService as SchedulerServiceImpl?)!!.schedule()
        verify { twitterClient.startFetchProcess() }
    }
}