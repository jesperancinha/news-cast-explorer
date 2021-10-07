package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.commands.consumer.CommandDispatcher
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration
import org.jesperancinha.newscast.orchestration.handlers.NewsCastAuthorCommentHandler
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.orchestration.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.orchestration.service.NewsCastTicketService
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


/**
 * Created by jofisaes on 06/10/2021
 */
@Configuration
@Import(SagaParticipantConfiguration::class,
    SagaOrchestratorConfiguration::class,
    TramMessageProducerJdbcConfiguration::class,
    EventuateTramKafkaMessageConsumerConfiguration::class)
open class NewsCastOrchestrationConfiguration(
) {
    @Bean("newsCastAuthorCommentHandler")
    open fun newsCastAuthorCommentHandler(newsCastAuthorCommentService: NewsCastAuthorCommentService): NewsCastAuthorCommentHandler {
        return NewsCastAuthorCommentHandler(newsCastAuthorCommentService)
    }

    @Bean
    open fun commandDispatcher(
        target: NewsCastAuthorCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): CommandDispatcher {
        return sagaCommandDispatcherFactory
            .make("newsCastAuthorCommentDispatcher", target.commandHandlerDefinitions())
    }

    @Bean
    open fun createCommentSaga(
        authorCommentRepository: AuthorCommentRepository,
    ): CreateCommentSaga {
        return CreateCommentSaga(authorCommentRepository)
    }

    @Bean("newsCastTicketService")
    open fun newsCastTicketService(
        sagaInstanceFactory: SagaInstanceFactory,
        createCommentSaga: CreateCommentSaga,
    ): NewsCastTicketService {
        return NewsCastTicketService(sagaInstanceFactory, createCommentSaga)
    }
}
