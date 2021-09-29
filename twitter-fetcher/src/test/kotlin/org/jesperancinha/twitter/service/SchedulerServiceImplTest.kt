package org.jesperancinha.twitter.service

import com.fasterxml.jackson.core.JsonProcessingException
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("scheduler")
@ExtendWith(MockitoExtension::class)
@Disabled
class SchedulerServiceImplTest {
    @Autowired
    private val runningService: RunningService? = null

    @MockBean
    private val twitterClient: TwitterClient? = null
    @BeforeEach
    fun setUp() {
        Mockito.reset(twitterClient)
    }

    @Test
    @Throws(InterruptedException::class, JsonProcessingException::class)
    fun testStartProcess_whenRunnerCreated_thenSchedulerServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(SchedulerServiceImpl::class.java)
        Mockito.verify(twitterClient, Mockito.never())?.startFetchProcess()
        Mockito.verifyNoMoreInteractions(twitterClient)
    }

    @Test
    @Throws(InterruptedException::class, JsonProcessingException::class)
    fun testStartProcess_whenRunnerStarted_thenSchedulerServiceStart() {
        runningService!!.startProcess()
        Mockito.verify(twitterClient, Mockito.only())?.startFetchProcess()
        Mockito.verifyNoMoreInteractions(twitterClient)
    }

    @Test
    @Throws(InterruptedException::class, JsonProcessingException::class)
    fun testStartProcess_whenSchedulerStarted_thenSchedulerServiceStarte() {
        (runningService as SchedulerServiceImpl?)!!.schedule()
        Mockito.verify(twitterClient, Mockito.only())?.startFetchProcess()
        Mockito.verifyNoMoreInteractions(twitterClient)
    }
}