package org.jesperancinha.twitter.client;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class TwitterClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        FetcherThread threadFetcher = new FetcherThread(
                "",
                "",
                "-",
                "");
        KillerThread killerThread = new KillerThread(service);
        service.submit(threadFetcher);
        service.submit(killerThread);
        service.shutdown();
    }
}
