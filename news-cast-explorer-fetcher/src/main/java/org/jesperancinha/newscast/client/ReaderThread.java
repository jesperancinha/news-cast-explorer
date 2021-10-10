package org.jesperancinha.newscast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.model.source.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Created by jofisaes on 05/10/2021
 */
@Component
@Builder
@Slf4j
public class ReaderThread extends Thread {

    private final String host;

    private final BlockingQueue<String> blockingQueue;

    private final ExecutorService executorService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReaderThread(
            @Value("${org.jesperancinha.newscast.host}")
                    String host, BlockingQueue<String> blockingQueue,
            ExecutorService executorService) {
        this.host = host;

        this.blockingQueue = blockingQueue;
        this.executorService = executorService;
    }

    @Override
    public void run() {

        try {
            for (; ; ) {
                sleep(1000);
                final ResponseEntity<Message[]> forEntity = restTemplate.getForEntity(host, Message[].class);
                Arrays.stream(forEntity.getBody()).toList().forEach(m -> {
                    try {
                        blockingQueue.add(objectMapper.writeValueAsString(m));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (InterruptedException e) {
            log.info("Reader task finished running!");
        } catch (Exception e) {
            log.error("An exception has occurred!", e);
        } finally {
            executorService.shutdownNow();
        }

    }
}
