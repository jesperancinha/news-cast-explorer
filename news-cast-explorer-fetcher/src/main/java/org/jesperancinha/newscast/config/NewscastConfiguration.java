package org.jesperancinha.newscast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class NewscastConfiguration {

    @Value("${org.jesperancinha.newscast.capacity}")
    private int capacity;

    @Bean
    public BlockingQueue<String> blockingQueue() {
        return new LinkedBlockingQueue<>(capacity);
    }
}
