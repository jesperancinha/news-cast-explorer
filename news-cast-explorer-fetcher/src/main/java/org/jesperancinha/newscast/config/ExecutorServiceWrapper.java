package org.jesperancinha.newscast.config;

import lombok.Setter;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by jofisaes on 13/10/2021
 */
@Service
public class ExecutorServiceWrapper {

    @Setter
    private ExecutorService executorService;
    private final BlockingQueueService blockingQueueService;
    private final long secondsDuration;
    private final String url;
    private FetcherCallable fetcherCallable;
    private final int timeToWaitSeconds;

    public ExecutorServiceWrapper(BlockingQueueService blockingQueueService,
                                  @Value("${org.jesperancinha.newscast.timeToWaitSeconds}") final int timeToWaitSeconds,
                                  @Value("${org.jesperancinha.newscast.timeToWaitSeconds}")
                                  long secondsDuration,
                                  @Value("${org.jesperancinha.newscast.host}")
                                  String url) throws InterruptedException {
        this.blockingQueueService = blockingQueueService;
        this.secondsDuration = secondsDuration;
        this.url = url;
        this.timeToWaitSeconds = timeToWaitSeconds;
        init();
    }

    private void init() throws InterruptedException {
        if (Objects.nonNull(executorService)) {
            executorService.shutdown();
            executorService.shutdownNow();
            if(!executorService.awaitTermination(timeToWaitSeconds, TimeUnit.SECONDS)){
                System.out.println("WARNING, termination seems to have not occurred gracefully");
            }
        }
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public ExecutorService executorService() {
        return this.executorService;
    }

    public ExecutorService restart() throws InterruptedException {
        init();
        this.fetcherCallable = createFetcherThread();
        blockingQueueService.init();
        executorService.submit(fetcherCallable);
        executorService.submit(createStopperThread());
        executorService.submit(createReaderThread());
        return executorService;
    }

    public FetcherCallable createFetcherThread() {
        return FetcherCallable.builder()
                .blockingQueueService(blockingQueueService)
                .executorServiceWrapper(this)
                .build();
    }

    private ReaderCallable createReaderThread() {
        return ReaderCallable.builder()
                .blockingQueueService(blockingQueueService)
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

}
