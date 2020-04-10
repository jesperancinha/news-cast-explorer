package org.jesperancinha.twitter.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for stopping the {@link ExecutorService} after a given amount of time in seconds.
 */
@Slf4j
@Builder
@AllArgsConstructor
public class KillerThread extends Thread {
    private final ExecutorService executorService;

    private final long secondsDuration;

    @Override
    public void run() {
        try {
            sleep();
        } catch (InterruptedException e) {
            log.error("An exception has ocurred!", e);
        } finally {
            executorService.shutdownNow();
            log.info("Well, it's time to go and sleep... :)");
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(secondsDuration));
    }
}
