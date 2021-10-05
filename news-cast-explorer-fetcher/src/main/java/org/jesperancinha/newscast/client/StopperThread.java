package org.jesperancinha.newscast.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for stopping the {@link ExecutorService} after a given amount of time in seconds.
 */
@Slf4j
@Builder
@Component
public class StopperThread extends Thread {


    private final long secondsDuration;

    private final ExecutorService executorService;

    public StopperThread(
            @Value("${org.jesperancinha.newscast.timeToWaitSeconds}")
                    long secondsDuration, ExecutorService executorService) {
        this.secondsDuration = secondsDuration;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        try {
            sleep();
        } catch (InterruptedException e) {
            log.error("An exception has occurred!", e);
        } finally {
            executorService.shutdownNow();
            log.info("Well, it's time to go and sleep... :)");
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(secondsDuration));
    }
}
