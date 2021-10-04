package org.jesperancinha.twitter.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.client.TwitterClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!scheduler")
public class OneRunServiceImpl implements RunningService {

    private final TwitterClient twitterClient;

    public OneRunServiceImpl(TwitterClient twitterClient) {
        this.twitterClient = twitterClient;
    }

    @Override
    public void startProcess() throws InterruptedException, JsonProcessingException {
        twitterClient.startFetchProcess();
    }
}
