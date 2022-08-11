package org.jesperancinha.newscast.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.config.ExecutorServiceWrapper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Responsible for stopping the {@link ExecutorService} after a given amount of time in seconds.
 */
@Slf4j
@Builder
public class StopperCallable implements Callable<Boolean> {

    private final long secondsDuration;

    private final ExecutorServiceWrapper executorServiceWrapper;

    public StopperCallable(
            long secondsDuration, ExecutorServiceWrapper executorServiceWrapper) {
        this.secondsDuration = secondsDuration;
        this.executorServiceWrapper = executorServiceWrapper;
    }

    @Override
    public Boolean call() {
        try {
            sleep(SECONDS.toMillis(secondsDuration));
        } catch (InterruptedException e) {
            log.error("An exception has occurred!", e);
            return false;
        } finally {
            executorServiceWrapper.executorService().shutdownNow();
            log.info("Well, it's time to go and sleep... :)");
        }
        return true;
    }
}
