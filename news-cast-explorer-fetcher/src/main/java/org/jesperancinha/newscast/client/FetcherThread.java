package org.jesperancinha.newscast.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@Component
public class FetcherThread extends Thread {

    private final Set<String> allMessages;
    private final ExecutorService executorService;
    private final ReaderThread readerThread;
    private final BlockingQueue<String> stringLinkedBlockingQueue;
    private final ReaderThread client;

    @Override
    public void run() {
        try {
            int msgRead = 0;
            while (msgRead < stringLinkedBlockingQueue.remainingCapacity()) {
                final String msg = stringLinkedBlockingQueue.poll(1, TimeUnit.SECONDS);
                msgRead = processMessage(msgRead, msg);
            }
        } catch (Exception e) {
            log.error("An exception has occurred!", e);
        } finally {
            executorService.shutdownNow();
        }
    }

    private int processMessage(int msgRead, String msg) {
        if (msg == null) {
            log.warn("Did not receive a message in 1 seconds");
        } else {
            msgRead = processIncomingTextMessage(msgRead, msg);
        }
        return msgRead;
    }

    private int processIncomingTextMessage(int msgRead, String msg) {
        allMessages.add(msg);
        log.trace("Received 1 message!");
        log.trace(msg);
        msgRead++;
        return msgRead;
    }

    public Set<String> getAllMessages() {
        return allMessages;
    }
}
