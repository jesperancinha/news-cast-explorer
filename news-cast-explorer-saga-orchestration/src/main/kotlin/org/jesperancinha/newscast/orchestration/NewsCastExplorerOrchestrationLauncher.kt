package org.jesperancinha.newscast.orchestration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Created by jofisaes on 06/10/2021
 */
@SpringBootApplication
@ComponentScan("org.jesperancinha.newscast")
class NewsCastExplorerChoreographyLauncher

fun main(args: Array<String>) {
    SpringApplication.run(NewsCastExplorerChoreographyLauncher::class.java, *args)
}