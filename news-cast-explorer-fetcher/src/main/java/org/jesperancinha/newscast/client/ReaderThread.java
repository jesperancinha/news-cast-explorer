package org.jesperancinha.newscast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.model.source.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
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

    private final String url;

    private final BlockingQueue<String> blockingQueue;

    private final ExecutorService executorService;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ReaderThread(
            @Value("${org.jesperancinha.newscast.host}")
                    String url, BlockingQueue<String> blockingQueue,
            ExecutorService executorService) {
        if (url.contains("news_cast_mock")) {
            final String news_cast_mock;
            try {
                news_cast_mock = InetAddress.getByName("news_cast_mock").getHostAddress();
                log.info(news_cast_mock);
                url = url.replace("news_cast_mock", news_cast_mock);
            } catch (UnknownHostException ignored) {

            }
        }
        this.url = url;

        log.info("Using url {}", url);
        this.blockingQueue = blockingQueue;
        this.executorService = executorService;
    }

    @Override
    public void run() {

        try {
            for (; ; ) {
                sleep(1000);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                final ResponseEntity<Message[]> result = restTemplate.exchange(url, HttpMethod.POST, entity, Message[].class);
                Arrays.stream(result.getBody()).toList().forEach(m -> {
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
