package org.jesperancinha.newscast.utils

import io.kotest.mpp.log
import io.mockk.impl.log.Logger
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgresSQLContainer>(imageName)

@Testcontainers
open class AbstractNCTest {
    companion object {
        private val logger = LoggerFactory.getLogger(AbstractNCTest::class.java)

        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:15beta2")
            .withUsername("postgres")
            .withPassword("admin")
            .withDatabaseName("eventuate")


        init {
            postgreSQLContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            logger.info { "Connection details to PostgreSQL:" }
            logger.info { "Server Address: ${postgreSQLContainer.host}" }
            logger.info { "Exposed port numbers: ${postgreSQLContainer.exposedPorts}" }
            registry.add("spring.datasource.url") { "jdbc:postgresql://${postgreSQLContainer.host}:${postgreSQLContainer.exposedPorts[0]}/eventuate" }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "admin" }
        }
    }
}