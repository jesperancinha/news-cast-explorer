package org.jesperancinha.newscast.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class BlockingQueueService {

    private final int capacity;

    @Getter
    private BlockingQueue<String> blockingQueue;

    public BlockingQueueService(@Value("${org.jesperancinha.newscast.capacity}")
                                int capacity) {
        this.capacity = capacity;
        init();
    }

    public void init() {
        this.blockingQueue = new LinkedBlockingQueue<>(capacity);
    }
}
