package org.jesperancinha.newscast.config;

import org.jesperancinha.newscast.client.FetcherCallable;
import org.jesperancinha.newscast.client.ReaderCallable;
import org.jesperancinha.newscast.client.StopperCallable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private final BlockingQueue<String> blockingQueue;
    private final long secondsDuration;
    private final String url;
    private FetcherCallable fetcherCallable;

    public ExecutorServiceWrapper(BlockingQueue<String> blockingQueue,
                                  @Value("${org.jesperancinha.newscast.timeToWaitSeconds}")
                                  long secondsDuration,
                                  @Value("${org.jesperancinha.newscast.host}")
                                  String url) {
        this.blockingQueue = blockingQueue;
        this.secondsDuration = secondsDuration;
        this.url = url;
        executorService = init();
    }

    private ExecutorService init() {
        if (Objects.nonNull(executorService)) {
            executorService.shutdownNow();
        }
        return Executors.newFixedThreadPool(3);
    }

    public ExecutorService executorService() {
        return this.executorService;
    }

    public ExecutorService restart() throws InterruptedException {
        executorService = init();
        this.fetcherCallable = createFetcherThread();
        executorService.invokeAll(
                List.of(fetcherCallable,
                        createReaderThread(),
                        createStopperThread()));
        return executorService;
    }

    public FetcherCallable createFetcherThread() {
        return FetcherCallable.builder()
                .stringLinkedBlockingQueue(blockingQueue)
                .executorServiceWrapper(this)
                .build();
    }

    private ReaderCallable createReaderThread() {
        return ReaderCallable.builder()
                .blockingQueue(blockingQueue)
                .executorServiceWrapper(this)
                .url(url)
                .build();
    }

    private StopperCallable createStopperThread() {
        return StopperCallable.builder()
                .secondsDuration(secondsDuration)
                .executorServiceWrapper(this)
                .build();
    }

    public FetcherCallable getFetcherThread() {
        return fetcherCallable;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }
}
