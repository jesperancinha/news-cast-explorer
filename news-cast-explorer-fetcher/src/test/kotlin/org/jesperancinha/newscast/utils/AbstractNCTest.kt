package org.jesperancinha.newscast.utils

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
            val postgresHost = postgreSQLContainer.host
            val postgresPort = postgreSQLContainer.getMappedPort(5432)
            logger.info { "Connection details to PostgreSQL:" }
            logger.info { "Server Address: $postgresHost" }
            logger.info { "Exposed port numbers: $postgresPort" }
            registry.add("spring.datasource.url") {
                "jdbc:postgresql://$postgresHost:$postgresPort/eventuate"
            }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "admin" }
        }
    }
}