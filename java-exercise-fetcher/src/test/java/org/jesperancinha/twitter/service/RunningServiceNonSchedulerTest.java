package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.client.TwitterClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
public class RunningServiceNonSchedulerTest {

    @Autowired
    private RunningService runningService;

    @MockBean
    private TwitterClient twitterClient;

    @Test
    public void testStartProcess_whenScheduler_thenSchedularInstance() throws InterruptedException {
        assertThat(runningService).isInstanceOf(OneRunServiceImpl.class);

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }
}