package org.jesperancinha.twitter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.client.TwitterClient;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("scheduler")
public class SchedulerServiceImpl implements RunningService {

    private final TwitterClient twitterClient;

    public SchedulerServiceImpl(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    @Scheduled(cron = "${org.jesperancinha.twitter.cron}")
    public void schedule() throws InterruptedException, JsonProcessingException {
        startProcess();
    }

    @Override
    public void startProcess() throws InterruptedException, JsonProcessingException {
        twitterClient.startFetchProcess();
    }
}
