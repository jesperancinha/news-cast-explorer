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

import static java.lang.Thread.sleep;

@Slf4j
@Getter
@Component
@Builder
public class NewsCastClient {

    private final String searchTerm;

    private final int timeToWaitSeconds;

    private final NewsCastMessageProcessor newsCastMessageProcessor;

    private final BlockingQueue<String> stringLinkedBlockingQueue;

    private final ExecutorServiceWrapper executorServiceWrapper;

    public NewsCastClient(
            @Value("${org.jesperancinha.newscast.searchTerm}")
            final String searchTerm,
            @Value("${org.jesperancinha.newscast.timeToWaitSeconds}")
            final int timeToWaitSeconds,
            NewsCastMessageProcessor newsCastMessageProcessor,
            BlockingQueue<String> stringLinkedBlockingQueue,
            ExecutorServiceWrapper executorServiceWrapper) {
        this.searchTerm = searchTerm;
        this.timeToWaitSeconds = timeToWaitSeconds;
        this.newsCastMessageProcessor = newsCastMessageProcessor;
        this.stringLinkedBlockingQueue = stringLinkedBlockingQueue;
        this.executorServiceWrapper = executorServiceWrapper;
    }

    /**
     * Returns a complete Dto fetch {@link PageDto} object, which contains all the data tweets for one run.
     * Authors {@link AuthorDto }are return by ascending order of creation.
     * Messages {@link MessageDto} are returned by ascending order of creation
     *
     * @return A {@link PageDto} object with all the data for one run
     */
    public synchronized PageDto startFetchProcess() throws JsonProcessingException, InterruptedException {
        val timestampBefore = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        val executorService = executorServiceWrapper.restart();
        try {
            sleep(TimeUnit.SECONDS.toMillis(timeToWaitSeconds));
            executorService.shutdownNow();
        } catch (Exception exception) {
            log.warn("Service tried to shutdown correctly, however an exception has occurred", exception);
            log.warn("Shutting down executor service...");
            executorService.shutdownNow();
        }
        val timestampAfter = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return newsCastMessageProcessor.processAllMessages(executorServiceWrapper.getFetcherThread().getAllMessages(), timestampBefore, timestampAfter);
    }

}