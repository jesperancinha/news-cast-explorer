package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.client.TwitterClient;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("scheduler")
public class SchedulerServiceImpl implements RunningService{

    private final TwitterClient twitterClient;

    public SchedulerServiceImpl(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    @Scheduled(cron = "${org.jesperancinha.twitter.cron}")
    public void scheduled() throws InterruptedException {
       startProcess();
    }

    private static int getCapacity(String[] args) {
        if (args.length > 5) {
            return Integer.parseInt(args[5]);
        }
        return 100;
    }

    private static int getTimeToWait(String[] args) {
        if (args.length > 6) {
            return Integer.parseInt(args[6]);
        }
        return 30;
    }

    @Override
    public void startProcess() throws InterruptedException {
        twitterClient.startFetchProcess();
    }
}
