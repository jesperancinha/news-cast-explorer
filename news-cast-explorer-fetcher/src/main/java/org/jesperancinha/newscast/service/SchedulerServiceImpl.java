package org.jesperancinha.newscast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.newscast.client.NewsCastClient;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("scheduler")
public class SchedulerServiceImpl implements RunningService {

    private final NewsCastClient newsCastClient;

    public SchedulerServiceImpl(NewsCastClient newsCastClient) {
        this.newsCastClient = newsCastClient;
    }

    @Scheduled(cron = "${org.jesperancinha.newscast.cron}")
    public void schedule() throws InterruptedException, JsonProcessingException {
        startProcess();
    }

    @Override
    public void startProcess() throws InterruptedException, JsonProcessingException {
        newsCastClient.startFetchProcess();
    }
}
