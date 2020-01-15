package org.jesperancinha.twitter.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@Profile("scheduler")
@Slf4j
public class TwitterFetcherLauncher implements CommandLineRunner {

    private TwitterClient twitterClient;

    public static void main(String[] args) throws InterruptedException {
        TwitterFetcherLauncher.getTwitterClientBuild(args).startFetchProcess();
        SpringApplication.run(TwitterFetcherLauncher.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        startFetchingWithArguments(args);
    }

    public void startFetchingWithArguments(String[] args) throws InterruptedException {
        twitterClient = getTwitterClientBuild(args);
    }

    private static TwitterClient getTwitterClientBuild(String[] args) {
        return TwitterClient.builder()
                .consumerKey(args[0])
                .consumerSecret(args[1])
                .token(args[2])
                .tokenSecret(args[3])
                .filterKey(args[4])
                .build();
    }

    @Scheduled(cron = "0 * * * * *")
    @Profile("scheduler")
    public void scheduled() throws InterruptedException {
        twitterClient.startFetchProcess();
    }
}
