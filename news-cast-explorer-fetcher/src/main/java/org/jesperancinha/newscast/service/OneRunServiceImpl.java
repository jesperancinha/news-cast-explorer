package org.jesperancinha.newscast.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.newscast.client.NewsCastClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!scheduler")
public class OneRunServiceImpl implements RunningService {

    private final NewsCastClient newsCastClient;

    public OneRunServiceImpl(NewsCastClient newsCastClient) {
        this.newsCastClient = newsCastClient;
    }

    @Override
    public void startProcess() throws InterruptedException, JsonProcessingException {
        newsCastClient.startFetchProcess();
    }
}
