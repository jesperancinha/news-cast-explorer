package org.jesperancinha.newscast.utils

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
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

        private const val POSTGRESQL_PORT = 5432

        @Container
        @JvmField
        val postgreSQLContainer: TestPostgresSQLContainer = TestPostgresSQLContainer("postgres:15beta2")
            .withUsername("postgres")
            .withPassword("admin")
            .withDatabaseName("eventuate")
            .withExposedPorts(POSTGRESQL_PORT)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(
                            Ports.Binding.bindPort(POSTGRESQL_PORT),
                            ExposedPort(POSTGRESQL_PORT)
                        )
                    )
                )
            }

        init {
            postgreSQLContainer.start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun setProperties(registry: DynamicPropertyRegistry) {
            logger.info { "Connection details to PostgreSQL:" }
            logger.info { "Bound port numbers: ${postgreSQLContainer.boundPortNumbers}" }
            logger.info { "Exposed port numbers: ${postgreSQLContainer.exposedPorts}" }
            registry.add("spring.datasource.url") { "jdbc:postgresql://localhost:${postgreSQLContainer.exposedPorts[0]}/eventuate" }
            registry.add("spring.datasource.username") { "postgres" }
            registry.add("spring.datasource.password") { "admin" }
        }
    }
}