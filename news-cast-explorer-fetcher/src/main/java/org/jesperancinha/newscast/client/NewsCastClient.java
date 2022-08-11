package org.jesperancinha.newscast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jesperancinha.newscast.config.ExecutorServiceWrapper;
import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.processor.NewsCastMessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Component
@Builder
public class NewsCastClient {

    private final String searchTerm;

    private final int timeToWaitSeconds;

    private final NewsCastMessageProcessor newsCastMessageProcessor;

    private final BlockingQueue<String> stringLinkedBlockingQueue;

    private final ReaderThread readerThread;
    private final FetcherThread fetcherThread;
    private final StopperThread stopperThread;

    private final ExecutorServiceWrapper executorServiceWrapper;

    public NewsCastClient(
            @Value("${org.jesperancinha.newscast.searchTerm}") final String searchTerm,
            @Value("${org.jesperancinha.newscast.timeToWaitSeconds}") final int timeToWaitSeconds,
            NewsCastMessageProcessor newsCastMessageProcessor,
            BlockingQueue<String> stringLinkedBlockingQueue,
            ReaderThread readerThread,
            FetcherThread fetcherThread,
            StopperThread stopperThread,
            ExecutorServiceWrapper executorServiceWrapper) {
        this.searchTerm = searchTerm;
        this.timeToWaitSeconds = timeToWaitSeconds;
        this.newsCastMessageProcessor = newsCastMessageProcessor;
        this.stringLinkedBlockingQueue = stringLinkedBlockingQueue;
        this.readerThread = readerThread;
        this.fetcherThread = fetcherThread;
        this.stopperThread = stopperThread;
        this.executorServiceWrapper = executorServiceWrapper;
    }

    /**
     * Returns a complete Dto fetch {@link PageDto} object, which contains all the data tweets for one run.
     * Authors {@link AuthorDto }are return by ascending order of creation.
     * Messages {@link MessageDto} are returned by ascending order of creation
     *
     * @return A {@link PageDto} object with all the data for one run
     * @throws InterruptedException This client {@link NewsCastClient} will throw this exception if interrupted
     */
    public PageDto startFetchProcess() throws InterruptedException, JsonProcessingException {
        val timestampBefore = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        val executorService = executorServiceWrapper.restart();
        executorService.execute(fetcherThread);
        executorService.execute(readerThread);
        executorService.execute(stopperThread);
        executorService.shutdown();
        while (!executorService.awaitTermination(timeToWaitSeconds, TimeUnit.SECONDS)) ;
        val timestampAfter = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return newsCastMessageProcessor.processAllMessages(fetcherThread.getAllMessages(), timestampBefore, timestampAfter);
    }

}