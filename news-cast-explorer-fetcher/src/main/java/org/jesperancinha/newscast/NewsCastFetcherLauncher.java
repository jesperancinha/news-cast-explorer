package org.jesperancinha.newscast;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.service.RunningService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(title = "OpenAPI definition"),
        servers = {@Server(url = "${nc.fetcher.server.url}",
                description = "Server URL")}
)
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


