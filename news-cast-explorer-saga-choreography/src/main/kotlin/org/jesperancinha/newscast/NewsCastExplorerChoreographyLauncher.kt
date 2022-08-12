package org.jesperancinha.newscast

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by jofisaes on 06/10/2021
 */
@SpringBootApplication
@OpenAPIDefinition(
    info = Info(title = "OpenAPI definition"),
    servers = [Server(url = "\${nc.choreography.server.url}", description = "Server URL")]
)
class NewsCastExplorerChoreographyLauncher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(NewsCastExplorerChoreographyLauncher::class.java, *args)
        }
    }
}

