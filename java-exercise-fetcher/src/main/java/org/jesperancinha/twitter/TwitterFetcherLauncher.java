package org.jesperancinha.twitter;

import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.client.TwitterClient;
import org.jesperancinha.twitter.client.TwitterClientImpl;
import org.jesperancinha.twitter.processor.TwitterMessageProcessorImpl;
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
        TwitterFetcherLauncher.getTwitterClientBuild(args, TwitterMessageProcessorImpl.getInstance()).startFetchProcess();
        SpringApplication.run(TwitterFetcherLauncher.class, args);
    }

    @Override
    public void run(String... args) {
        twitterClient = createClientFromArgs(args);
    }

    TwitterClientImpl createClientFromArgs(String[] args) {
        return getTwitterClientBuild(args, TwitterMessageProcessorImpl.getInstance());
    }

    private static TwitterClientImpl getTwitterClientBuild(String[] args,
                                                           TwitterMessageProcessorImpl twitterMessageProcessorImpl) {
        return TwitterClientImpl.builder()
                .consumerKey(args[0])
                .consumerSecret(args[1])
                .token(args[2])
                .tokenSecret(args[3])
                .searchTerm(args[4])
                .capacity(getCapacity(args))
                .timeToWaitSeconds(getTimeToWait(args))
                .twitterMessageProcessor(twitterMessageProcessorImpl)
                .build();
    }

    @Scheduled(cron = "${org.jesperancinha.twitter.cron}")
    @Profile("scheduler")
    public void scheduled() throws InterruptedException {
        twitterClient.startFetchProcess();
    }

    private static int getCapacity(String[] args) {
        if (args.length > 5) {
            return Integer.parseInt(args[5]);
        }
        return 100;
    }

    private static int getTimeToWait(String[] args) {
        if (args.length > 6) {
            return Integer.parseInt(args[6]);
        }
        return 30;
    }
}
