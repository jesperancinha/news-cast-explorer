package org.jesperancinha.newscast.config;

import org.jesperancinha.newscast.client.ReaderThread;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class NewscastConfiguration {

    @Value("${org.jesperancinha.newscast.capacity}")
    private int capacity;

    @Bean
    public BlockingQueue<String> blockingQueue() {
        return new LinkedBlockingQueue<>(capacity);
    }

    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(3);
    }
}
