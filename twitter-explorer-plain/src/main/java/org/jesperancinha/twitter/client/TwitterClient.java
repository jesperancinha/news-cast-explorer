package org.jesperancinha.twitter.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.jesperancinha.twitter.data.PageDto;

public interface TwitterClient {
    PageDto startFetchProcess() throws InterruptedException, JsonProcessingException;
}
