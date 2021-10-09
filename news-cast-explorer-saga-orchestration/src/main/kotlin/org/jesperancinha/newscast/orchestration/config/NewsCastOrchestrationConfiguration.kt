package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.commands.consumer.CommandDispatcher
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.orchestration.SagaOrchestratorConfiguration
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import io.eventuate.tram.spring.consumer.kafka.EventuateTramKafkaMessageConsumerConfiguration
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration
import org.jesperancinha.newscast.orchestration.handlers.NewsCastAuthorCommentHandler
import org.jesperancinha.newscast.orchestration.handlers.NewsCastMessageCommentHandler
import org.jesperancinha.newscast.orchestration.handlers.NewsCastPageCommentHandler
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.orchestration.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.orchestration.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.orchestration.service.NewsCastPageCommentService
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
open class NewsCastOrchestrationConfiguration {
    @Bean("newsCastPageCommentHandler")
    open fun newsCastPageCommentHandler(newsCastPageCommentService: NewsCastPageCommentService): NewsCastPageCommentHandler {
        return NewsCastPageCommentHandler(newsCastPageCommentService)
    }

    @Bean("newsCastAuthorCommentHandler")
    open fun newsCastAuthorCommentHandler(newsCastAuthorCommentService: NewsCastAuthorCommentService): NewsCastAuthorCommentHandler {
        return NewsCastAuthorCommentHandler(newsCastAuthorCommentService)
    }

    @Bean("newsCastMessageCommentHandler")
    open fun newsCastMessageCommentHandler(newsCastMessageCommentService: NewsCastMessageCommentService): NewsCastMessageCommentHandler {
        return NewsCastMessageCommentHandler(newsCastMessageCommentService)
    }

    @Bean
    open fun commandDispatcher(
        newsCastPageCommentHandler: NewsCastPageCommentHandler,
        newsCastAuthorCommentHandler: NewsCastAuthorCommentHandler,
        newsCastMessageCommentHandler: NewsCastMessageCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): CommandDispatcher {
        return sagaCommandDispatcherFactory
            .make("newsCastCommentDispatcher",
                CommandHandlers(
                    newsCastPageCommentHandler.commandHandlerDefinitions().handlers +
                            newsCastAuthorCommentHandler.commandHandlerDefinitions().handlers +
                            newsCastMessageCommentHandler.commandHandlerDefinitions().handlers
                )
            )
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
