package org.jesperancinha.twitter.client;

import org.jesperancinha.twitter.data.PageDto;

public interface TwitterClient {
    PageDto startFetchProcess() throws InterruptedException;
}
