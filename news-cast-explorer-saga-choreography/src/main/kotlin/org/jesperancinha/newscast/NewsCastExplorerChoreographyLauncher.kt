package org.jesperancinha.newscast

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by jofisaes on 06/10/2021
 */
@SpringBootApplication
open class NewsCastExplorerChoreographyLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(NewsCastExplorerChoreographyLauncher::class.java, *args)
        }
    }
}

