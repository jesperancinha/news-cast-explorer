package org.jesperancinha.twitter.service

import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
@Disabled
class OneRunServiceImplTest {
    @Autowired
    private val runningService: RunningService? = null

    @MockBean
    private val twitterClient: TwitterClient? = null
    @BeforeEach
    fun setUp() {
        Mockito.reset(twitterClient)
    }

    @Test
    fun testStartProcess_whenRunnerCreated_thenOneServiceInstance() {
        Assertions.assertThat(runningService).isInstanceOf(OneRunServiceImpl::class.java)
        Mockito.verify(twitterClient, Mockito.never())?.startFetchProcess()
        Mockito.verifyNoMoreInteractions(twitterClient)
    }

    @Test
    fun testStartProcess_whenRunnerStarted_thenOneServiceStart() {
        runningService!!.startProcess()
        Mockito.verify(twitterClient, Mockito.only())?.startFetchProcess()
        Mockito.verifyNoMoreInteractions(twitterClient)
    }
}