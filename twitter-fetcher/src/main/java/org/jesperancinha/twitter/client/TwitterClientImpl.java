package org.jesperancinha.twitter.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.processor.TwitterMessageProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Component
@Builder
public class TwitterClientImpl implements TwitterClient {

    private final int timeToWaitSeconds;

    private final TwitterMessageProcessor twitterMessageProcessor;

    private final Authentication authentication;

    private final BlockingQueue<String> stringLinkedBlockingQueue;

    private final List<String> searchTerms;

    public TwitterClientImpl(
            @Value("${org.jesperancinha.twitter.timeToWaitSeconds}")
                    int timeToWaitSeconds, TwitterMessageProcessor twitterMessageProcessor,
            Authentication authentication,
            BlockingQueue<String> stringLinkedBlockingQueue,
            List<String> searchTerms) {
        this.timeToWaitSeconds = timeToWaitSeconds;
        this.twitterMessageProcessor = twitterMessageProcessor;
        this.authentication = authentication;
        this.stringLinkedBlockingQueue = stringLinkedBlockingQueue;
        this.searchTerms = searchTerms;
    }

    /**
     * Returns a complete Dto fetch {@link PageDto} object, which contains all the data tweets for one run.
     * Authors {@link AuthorDto }are return by ascending order of creation.
     * Messages {@link MessageDto} are returned by ascending order of creation
     *
     * @return A {@link PageDto} object with all the data for one run
     * @throws InterruptedException This client {@link TwitterClientImpl} will throw this exception if interrupted
     */
    public PageDto startFetchProcess() throws InterruptedException, JsonProcessingException {
        final var executorService = Executors.newFixedThreadPool(2);
        final var allMessages = new HashSet<String>();
        final var threadFetcher = createFetcherThread(executorService, allMessages);
        final var killerThread = createKillerThread(executorService);
        final long timestampBefore = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        executorService.submit(killerThread);
        executorService.submit(threadFetcher);
        executorService.shutdown();
        executorService.awaitTermination(timeToWaitSeconds, TimeUnit.SECONDS);
        final long timestampAfter = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        return twitterMessageProcessor.processAllMessages(allMessages, timestampBefore, timestampAfter);
    }

    /**
     * Creates a Killer thread. {@link KillerThread}.
     *
     * @param executorService The {@link ExecutorService} responsible for the pool of threads
     * @return A {@link KillerThread} thread.
     */
    private KillerThread createKillerThread(ExecutorService executorService) {
        return KillerThread.builder()
                .executorService(executorService)
                .secondsDuration(timeToWaitSeconds)
                .build();
    }

    private FetcherThread createFetcherThread(ExecutorService executorService, Set<String> allMessages) {

        final var httpHosts = new HttpHosts(Constants.STREAM_HOST);
        final var statusesFilterEndpoint = new StatusesFilterEndpoint();
        statusesFilterEndpoint.trackTerms(searchTerms);

        final BasicClient client = new ClientBuilder()
                .hosts(httpHosts)
                .endpoint(statusesFilterEndpoint)
                .authentication(authentication)
                .processor(new StringDelimitedProcessor(stringLinkedBlockingQueue))
                .build();

        return FetcherThread.builder()
                .allMessages(allMessages)
                .executorService(executorService)
                .capacity(stringLinkedBlockingQueue.remainingCapacity())
                .client(client)
                .stringLinkedBlockingQueue(stringLinkedBlockingQueue)
                .build();
    }
}
