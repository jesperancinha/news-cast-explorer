package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.commands.common.CommandNameMapping
import io.eventuate.tram.commands.common.DefaultCommandNameMapping
import io.eventuate.tram.commands.consumer.CommandDispatcher
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.consumer.common.DuplicateMessageDetector
import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector
import io.eventuate.tram.messaging.common.ChannelMapping
import io.eventuate.tram.messaging.common.DefaultChannelMapping
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
import org.jesperancinha.newscast.orchestration.service.NewsCastTicketService
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import org.jesperancinha.newscast.service.AuthorService
import org.jesperancinha.newscast.service.MessageService
import org.jesperancinha.newscast.service.PageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


/**
 * Created by jofisaes on 06/10/2021
 */
@Configuration
@Import(
    SagaParticipantConfiguration::class,
    SagaOrchestratorConfiguration::class,
    TramMessageProducerJdbcConfiguration::class,
    EventuateTramKafkaMessageConsumerConfiguration::class
)
open class NewsCastOrchestrationConfiguration {
    @Bean("newsCastPageCommentHandler")
    open fun newsCastPageCommentHandler(
        newsCastPageCommentService: NewsCastPageCommentService,
        pageService: PageService,
    ): NewsCastPageCommentHandler {
        return NewsCastPageCommentHandler(newsCastPageCommentService, pageService)
    }

    @Bean("newsCastAuthorCommentHandler")
    open fun newsCastAuthorCommentHandler(
        newsCastAuthorCommentService: NewsCastAuthorCommentService,
        authorService: AuthorService,
    ): NewsCastAuthorCommentHandler {
        return NewsCastAuthorCommentHandler(
            newsCastAuthorCommentService,
            authorService
        )
    }

    @Bean("newsCastMessageCommentHandler")
    open fun newsCastMessageCommentHandler(
        newsCastMessageCommentService: NewsCastMessageCommentService,
        messageService: MessageService,
    ): NewsCastMessageCommentHandler {
        return NewsCastMessageCommentHandler(newsCastMessageCommentService, messageService)
    }

    @Bean
    open fun commandDispatcher(
        newsCastPageCommentHandler: NewsCastPageCommentHandler,
        newsCastAuthorCommentHandler: NewsCastAuthorCommentHandler,
        newsCastMessageCommentHandler: NewsCastMessageCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): CommandDispatcher {
        return sagaCommandDispatcherFactory
            .make(
                "newsCastCommentDispatcher",
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
        return CreateCommentSaga()
    }

    @Bean("newsCastTicketService")
    open fun newsCastTicketService(
        sagaInstanceFactory: SagaInstanceFactory,
        createCommentSaga: CreateCommentSaga,
    ): NewsCastTicketService {
        return NewsCastTicketService(sagaInstanceFactory, createCommentSaga)
    }

    @Bean
    open fun channelMapping(): ChannelMapping? = DefaultChannelMapping.builder().build()

    @Bean
    open fun commandNameMapping(): CommandNameMapping = DefaultCommandNameMapping()

    @Bean
    open fun duplicateMessageDetector(): DuplicateMessageDetector = NoopDuplicateMessageDetector()
}
