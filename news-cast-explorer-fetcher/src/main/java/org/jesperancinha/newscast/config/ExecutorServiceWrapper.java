package org.jesperancinha.newscast.config;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jofisaes on 13/10/2021
 */
public class ExecutorServiceWrapper {

    private ExecutorService executorService;

    ExecutorServiceWrapper() {
        init();
    }

    private void init() {
        if(Objects.nonNull(this.executorService)){
            this.executorService.shutdown();
        }
        this.executorService = Executors.newFixedThreadPool(3);
    }

    public ExecutorServiceWrapper(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ExecutorService executorService() {
        return this.executorService;
    }

    public void restart(){
        init();
    }
}
