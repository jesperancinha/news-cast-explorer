package org.jesperancinha.twitter;

import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.service.RunningService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class TwitterFetcherLauncher implements CommandLineRunner {

    private final RunningService runningService;

    public TwitterFetcherLauncher(RunningService runningService) {
        this.runningService = runningService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterFetcherLauncher.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        runningService.startProcess();
    }
}
