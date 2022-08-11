package org.jesperancinha.newscast.config;

import org.jesperancinha.newscast.client.FetcherThread;
import org.jesperancinha.newscast.client.ReaderThread;
import org.jesperancinha.newscast.client.StopperThread;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jofisaes on 13/10/2021
 */
@Service
public class ExecutorServiceWrapper {

    private ExecutorService executorService;
    private BlockingQueue<String> blockingQueue;
    private long secondsDuration;
    private String url;
    private FetcherThread fetcherThread;

    public ExecutorServiceWrapper(BlockingQueue<String> blockingQueue,
                                  @Value("${org.jesperancinha.newscast.timeToWaitSeconds}")
                                  long secondsDuration,
                                  @Value("${org.jesperancinha.newscast.host}")
                                  String url) {
        this.blockingQueue = blockingQueue;
        this.secondsDuration = secondsDuration;
        this.url = url;
    }

    private void init() {
        if (Objects.nonNull(this.executorService)) {
            this.executorService.shutdownNow();
        }
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public ExecutorService executorService() {
        return this.executorService;
    }

    public ExecutorService restart() {
        init();
        this.fetcherThread = createFetcherThread();
        executorService.execute(fetcherThread);
        executorService.execute(createReaderThread());
        executorService.execute(createStopperThread());
        return this.executorService;
    }

    public FetcherThread createFetcherThread() {
        return FetcherThread.builder()
                .stringLinkedBlockingQueue(blockingQueue)
                .executorServiceWrapper(this)
                .build();
    }

    private ReaderThread createReaderThread() {
        return ReaderThread.builder()
                .blockingQueue(blockingQueue)
                .executorServiceWrapper(this)
                .url(url)
                .build();
    }

    private StopperThread createStopperThread() {
        return StopperThread.builder()
                .secondsDuration(secondsDuration)
                .executorServiceWrapper(this)
                .build();
    }

    public FetcherThread getFetcherThread() {
        return fetcherThread;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
