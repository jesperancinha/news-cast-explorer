package org.jesperancinha.twitter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.client.TwitterClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
@ActiveProfiles("scheduler")
@ExtendWith(MockitoExtension.class)
@Disabled
public class SchedulerServiceImplTest {

    @Autowired
    private RunningService runningService;

    @MockBean
    private TwitterClient twitterClient;

    @BeforeEach
    public void setUp() {
        Mockito.reset(twitterClient);
    }

    @Test
    public void testStartProcess_whenRunnerCreated_thenSchedulerServiceInstance() throws InterruptedException, JsonProcessingException {
        assertThat(runningService).isInstanceOf(SchedulerServiceImpl.class);

        verify(twitterClient, never()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }

    @Test
    public void testStartProcess_whenRunnerStarted_thenSchedulerServiceStart() throws InterruptedException, JsonProcessingException {
        runningService.startProcess();

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }

    @Test
    public void testStartProcess_whenSchedulerStarted_thenSchedulerServiceStarte() throws InterruptedException, JsonProcessingException {
        ((SchedulerServiceImpl) runningService).schedule();

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }
}