package org.jesperancinha.twitter

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by jofisaes on 01/10/2021
 */
@SpringBootApplication
class TwitterMockLauncher

fun main(args: Array<String>) {
    SpringApplication.run(TwitterMockLauncher::class.java, *args)
}