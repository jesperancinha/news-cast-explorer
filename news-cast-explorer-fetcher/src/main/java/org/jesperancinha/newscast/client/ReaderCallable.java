package org.jesperancinha.newscast.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jesperancinha.newscast.config.BlockingQueueService;
import org.jesperancinha.newscast.config.ExecutorServiceWrapper;
import org.jesperancinha.newscast.model.source.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import static java.lang.Thread.enumerate;
import static java.lang.Thread.sleep;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

/**
 * Created by jofisaes on 05/10/2021
 */
@Builder
@Slf4j
public class ReaderCallable implements Callable<Boolean> {

    private final String url;

    private final BlockingQueueService blockingQueueService;

    private final ExecutorServiceWrapper executorServiceWrapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Logger logger = LoggerFactory.getLogger(ReaderCallable.class);

    public ReaderCallable(
            String url,
            BlockingQueueService blockingQueueService,
            ExecutorServiceWrapper executorServiceWrapper) {
        if (url.contains("news-cast-mock")) {
            final String newsCastMock;
            try {
                newsCastMock = InetAddress.getByName("news-cast-mock").getHostAddress();
                log.info(newsCastMock);
                url = url.replace("news-cast-mock", newsCastMock);
            } catch (UnknownHostException ignored) {

            }
        }
        this.url = url;

        log.info("Using url {}", url);
        this.blockingQueueService = blockingQueueService;
        this.executorServiceWrapper = executorServiceWrapper;
    }

    @Override
    public Boolean call() {
        BlockingQueue<String> blockingQueue = blockingQueueService.getBlockingQueue();
        try {
            for (; ; ) {
                sleep(1000);
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                final ResponseEntity<Message[]> result = restTemplate.exchange(
                        url, HttpMethod.POST,
                        new HttpEntity<String>(headers), Message[].class);
                stream(requireNonNull(result.getBody())).toList().forEach(m -> {
                    try {
                        if (blockingQueue.size() < 100) {
                            blockingQueue.add(objectMapper.writeValueAsString(m));
                        } else {
                            logger.warn("The queue is full for object {}", m);
                        }
                    } catch (JsonProcessingException e) {
                        logger.error("An error has occured processing json - {}", m, e);
                    } catch (Throwable throwable) {
                        logger.error("A CRITICAL error has occured processing json - {}", m, throwable);
                    }
                });
            }
        } catch (InterruptedException e) {
            log.info("Reader task finished running!");
            return true;
        } catch (Exception e) {
            log.error("An exception has occurred!", e);
            return false;
        }
    }
}
