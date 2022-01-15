package org.jesperancinha.newscast.utils

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

class TestPostgresSQLContainer(imageName: String) : PostgreSQLContainer<TestPostgresSQLContainer>(imageName)

abstract class AbstractNCTest {
    companion object {
        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:12")
            .withUsername("postgres")
            .withPassword("admin")
            .withDatabaseName("eventuate")


        init {
            postgreSQLContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { "jdbc:postgresql://localhost:${postgreSQLContainer.firstMappedPort}/eventuate" }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "admin" }
        }
    }
}