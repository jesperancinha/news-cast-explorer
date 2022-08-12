package org.jesperancinha.newscast

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by jofisaes on 01/10/2021
 */
@SpringBootApplication
class NewsCastMockLauncher

fun main(args: Array<String>) {
    SpringApplication.run(NewsCastMockLauncher::class.java, *args)
}