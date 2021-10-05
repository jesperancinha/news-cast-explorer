package org.jesperancinha.newscast;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.service.RunningService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class NewsCastFetcherLauncher implements CommandLineRunner {

    private final RunningService runningService;

    public NewsCastFetcherLauncher(RunningService runningService) {
        this.runningService = runningService;
    }

    public static void main(String[] args) {
        SpringApplication.run(NewsCastFetcherLauncher.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException, JsonProcessingException {
        runningService.startProcess();
    }
}


