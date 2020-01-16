package org.jesperancinha.twitter.client;

import com.twitter.hbc.httpclient.BasicClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Builder
@AllArgsConstructor
public class FetcherThread extends Thread {

    private final List<String> allMessages;
    private final ExecutorService executorService;
    private final int capacity;
    private final BasicClient client;
    private final BlockingQueue<String> stringLinkedBlockingQueue;

    @Override
    public void run() {
        try {
            client.connect();
            int msgRead = 0;
            while (msgRead < capacity) {
                if (client.isDone()) {
                    log.error("Client connection closed unexpectedly: " + client.getExitEvent().getMessage());
                    break;
                }
                final String msg = stringLinkedBlockingQueue.poll(1, TimeUnit.SECONDS);
                if (msg == null) {
                    log.warn("Did not receive a message in 1 seconds");
                } else {
                    allMessages.add(msg);
                    log.warn("Received 1 message!");
                    log.trace(msg);
                    msgRead++;
                }
            }
            client.stop();
        } catch (InterruptedException e) {
            log.error("An exception has ocurred!", e);
        } finally {
            executorService.shutdownNow();
        }
    }

}
