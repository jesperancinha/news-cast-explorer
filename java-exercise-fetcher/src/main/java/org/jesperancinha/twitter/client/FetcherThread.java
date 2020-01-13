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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FetcherThread extends Thread {
    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;
    private final List<String> allMessages;


    public FetcherThread(String consumerKey, String consumerSecret, String token, String tokenSecret, List<String> allMessages) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.allMessages = allMessages;
    }

    @SneakyThrows
    @Override
    public void run() {
        final Authentication auth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);
        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(100);
        final Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        final StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        final List<String> searchTerms = List.of("ariana grande");
        hosebirdEndpoint.trackTerms(searchTerms);
        final BasicClient client = new ClientBuilder()
                .hosts(hosebirdHosts)
                .endpoint(hosebirdEndpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();
        client.connect();
        for (int msgRead = 0; msgRead < 100; msgRead++) {
            if (client.isDone()) {
                log.error("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
                break;
            }
            String msg = queue.poll(5, TimeUnit.SECONDS);
            if (msg == null) {
                log.warn("Did not receive a message in 5 seconds");
            } else {
                allMessages.add(msg);
                log.info(msg);
            }
        }
        client.stop();
    }

}
