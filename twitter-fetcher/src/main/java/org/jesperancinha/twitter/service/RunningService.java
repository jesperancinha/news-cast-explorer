package org.jesperancinha.twitter.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RunningService {

    void startProcess() throws InterruptedException, JsonProcessingException;
}
