package org.jesperancinha.twitter.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class KillerThread extends Thread {
    private final ExecutorService executorService;

    public KillerThread(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
        executorService.shutdownNow();
        log.info("Well, it's time to go and sleep... :)");
    }
}
