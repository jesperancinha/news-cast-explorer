package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.TwitterFetcherLauncher;
import org.jesperancinha.twitter.client.TwitterClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest(classes = TwitterFetcherLauncher.class)
@ActiveProfiles("scheduler")
@ExtendWith(MockitoExtension.class)
public class SchedulerServiceImplTest {

    private final RunningService runningService;

    public SchedulerServiceImplTest(RunningService runningService) {
        this.runningService = runningService;
    }

    @MockBean
    private TwitterClient twitterClient;

    @BeforeEach
    public void setUp() {
        Mockito.reset(twitterClient);
    }

    @Test
    public void testStartProcess_whenRunnerCreated_thenSchedulerServiceInstance() throws InterruptedException {
        assertThat(runningService).isInstanceOf(SchedulerServiceImpl.class);

        verify(twitterClient, never()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }

    @Test
    public void testStartProcess_whenRunnerStarted_thenSchedulerServiceStart() throws InterruptedException {
        runningService.startProcess();

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }

    @Test
    public void testStartProcess_whenSchedulerStarted_thenSchedulerServiceStarte() throws InterruptedException {
        ((SchedulerServiceImpl)runningService).schedule();

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }
}