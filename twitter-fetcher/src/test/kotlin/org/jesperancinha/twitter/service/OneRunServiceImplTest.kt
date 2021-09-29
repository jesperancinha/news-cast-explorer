package org.jesperancinha.twitter.service

import com.ninjasquad.springmockk.MockkBean
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class OneRunServiceImplTest(
    @Autowired
    val runningService: RunningService,
) {
    @MockkBean(relaxed = true)
    lateinit var twitterClient: TwitterClient

    @Test
    fun testStartProcess_whenRunnerCreated_thenOneServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(OneRunServiceImpl::class.java)
        verify { twitterClient.startFetchProcess() }
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenOneServiceStart() {
        runningService.startProcess()
        verify { twitterClient.startFetchProcess() }
    }
}