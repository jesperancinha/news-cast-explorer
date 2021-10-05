package org.jesperancinha.newscast.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by jofisaes on 05/10/2021
 */
@Component
@Builder
@Slf4j
public class ReaderThread extends Thread {

    private final String host;

    private final BlockingQueue<String> blockingQueue;

    private final ExecutorService executorService;

    public ReaderThread(
            @Value("${org.jesperancinha.newscast.host}")
                    String host, BlockingQueue<String> blockingQueue,
            ExecutorService executorService) {
        this.host = host;

        this.blockingQueue = blockingQueue;
        this.executorService = executorService;
    }

    public void connect() {

    }

    public boolean isDone() {
        return false;
    }

    public Throwable getExitEvent() {
        return null;
    }

    @Override
    public void run() {

        try {
            for (; ; ) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("An exception has occurred!", e);
        } finally {
            executorService.shutdownNow();
        }

    }
}
