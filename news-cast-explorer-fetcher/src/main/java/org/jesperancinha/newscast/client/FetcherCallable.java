package org.jesperancinha.newscast.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.config.ExecutorServiceWrapper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
public class FetcherCallable implements Callable<Boolean> {

    private Set<String> allMessages;
    private final ExecutorServiceWrapper executorServiceWrapper;
    private final ReaderCallable readerCallable;
    private final BlockingQueue<String> stringLinkedBlockingQueue;

    @Override
    public Boolean call() {
        allMessages = new HashSet<>();
        try {
            int msgRead = 0;
            while (msgRead < stringLinkedBlockingQueue.remainingCapacity()) {
                final String msg = stringLinkedBlockingQueue.poll(1, TimeUnit.SECONDS);
                msgRead = processMessage(msgRead, msg);
            }
        } catch (Exception e) {
            log.error("An exception has occurred!", e);
            return false;
        } finally {
            executorServiceWrapper.executorService().shutdownNow();
        }
        return true;
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
