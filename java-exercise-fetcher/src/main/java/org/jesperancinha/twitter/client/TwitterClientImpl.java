package org.jesperancinha.twitter.client;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.processor.TwitterMessageProcessor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@AllArgsConstructor
@Getter
public class TwitterClientImpl implements TwitterClient {

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;
    private final String searchTerm;
    private final int capacity;
    private final int timeToWaitSeconds;
    private final TwitterMessageProcessor twitterMessageProcessor;

    /**
     * Returns a complete Dto fetch {@link PageDto} object, which contains all the data tweets for one run.
     * Authors {@link AuthorDto }are return by ascending order of creation.
     * Messages {@link MessageDto} are returned by ascending order of creation
     *
     * @return A {@link PageDto} object with all the data for one run
     * @throws InterruptedException This client {@link TwitterClientImpl} will throw this exception if interrupted
     */
    public PageDto startFetchProcess() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final List<String> allMessages = new ArrayList<>();
        final FetcherThread threadFetcher = createFetcherThread(executorService, allMessages);
        final KillerThread killerThread = createKillerThread(executorService);
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

    private FetcherThread createFetcherThread(ExecutorService executorService, List<String> allMessages) {

        final Authentication auth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);
        final BlockingQueue<String> stringLinkedBlockingQueue = new LinkedBlockingQueue<>(capacity);
        final Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        final StatusesFilterEndpoint statusesFilterEndpoint = new StatusesFilterEndpoint();
        final List<String> searchTerms = List.of(searchTerm);
        statusesFilterEndpoint.trackTerms(searchTerms);

        final BasicClient client = new ClientBuilder()
                .hosts(hosebirdHosts)
                .endpoint(statusesFilterEndpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(stringLinkedBlockingQueue))
                .build();

        return FetcherThread.builder()
                .allMessages(allMessages)
                .executorService(executorService)
                .capacity(capacity)
                .client(client)
                .stringLinkedBlockingQueue(stringLinkedBlockingQueue)
                .build();
    }
}
