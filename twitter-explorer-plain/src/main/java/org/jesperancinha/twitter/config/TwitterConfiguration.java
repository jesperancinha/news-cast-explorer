package org.jesperancinha.twitter.config;

import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class TwitterConfiguration {
    @Value("${org.jesperancinha.twitter.consumerKey}")
    private String consumerKey;

    @Value("${org.jesperancinha.twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${org.jesperancinha.twitter.token}")
    private String token;

    @Value("${org.jesperancinha.twitter.tokenSecret}")
    private String tokenSecret;

    @Value("${org.jesperancinha.twitter.capacity}")
    private int capacity;

    @Bean
    public OAuth1 authentication() {
        return new OAuth1(consumerKey, consumerSecret, token, tokenSecret);
    }

    @Bean
    @Scope(value = "prototype")
    public BlockingQueue<String> blockingQueue() {
        return new LinkedBlockingQueue<>(capacity);
    }

}
