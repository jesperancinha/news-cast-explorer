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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest(classes = TwitterFetcherLauncher.class)
@ExtendWith(MockitoExtension.class)
public class OneRunServiceImplTest {

    private final RunningService runningService;

    public OneRunServiceImplTest(RunningService runningService) {
        this.runningService = runningService;
    }

    @MockBean
    private TwitterClient twitterClient;

    @BeforeEach
    public void setUp() {
        Mockito.reset(twitterClient);
    }

    @Test
    public void testStartProcess_whenRunnerCreated_thenOneServiceInstance() throws InterruptedException {
        assertThat(runningService).isInstanceOf(OneRunServiceImpl.class);

        verify(twitterClient, never()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }

    @Test
    public void testStartProcess_whenRunnerStarted_thenOneServiceStart() throws InterruptedException {
        runningService.startProcess();

        verify(twitterClient, only()).startFetchProcess();
        verifyNoMoreInteractions(twitterClient);

    }
}