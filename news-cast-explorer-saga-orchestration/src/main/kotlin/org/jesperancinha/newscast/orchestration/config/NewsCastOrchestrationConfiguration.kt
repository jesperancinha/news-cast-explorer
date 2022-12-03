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
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher
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
class NewsCastOrchestrationConfiguration {
    @Bean("newsCastPageCommentHandler")
    fun newsCastPageCommentHandler(
        newsCastPageCommentService: NewsCastPageCommentService,
        pageService: PageService,
    ): NewsCastPageCommentHandler = NewsCastPageCommentHandler(newsCastPageCommentService, pageService)

    @Bean("newsCastAuthorCommentHandler")
    fun newsCastAuthorCommentHandler(
        newsCastAuthorCommentService: NewsCastAuthorCommentService,
        authorService: AuthorService,
    ): NewsCastAuthorCommentHandler = NewsCastAuthorCommentHandler(
        newsCastAuthorCommentService,
        authorService
    )

    @Bean("newsCastMessageCommentHandler")
    fun newsCastMessageCommentHandler(
        newsCastMessageCommentService: NewsCastMessageCommentService,
        messageService: MessageService,
    ): NewsCastMessageCommentHandler = NewsCastMessageCommentHandler(newsCastMessageCommentService, messageService)

    @Bean
    fun commandDispatcher(
        newsCastPageCommentHandler: NewsCastPageCommentHandler,
        newsCastAuthorCommentHandler: NewsCastAuthorCommentHandler,
        newsCastMessageCommentHandler: NewsCastMessageCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): SagaCommandDispatcher = sagaCommandDispatcherFactory
        .make(
            "newsCastCommentDispatcher",
            CommandHandlers(
                newsCastPageCommentHandler.commandHandlerDefinitions().handlers +
                        newsCastAuthorCommentHandler.commandHandlerDefinitions().handlers +
                        newsCastMessageCommentHandler.commandHandlerDefinitions().handlers
            )
        )

    @Bean
    fun createCommentSaga(
        authorCommentRepository: AuthorCommentRepository,
    ): CreateCommentSaga = CreateCommentSaga()

    @Bean("newsCastTicketService")
    fun newsCastTicketService(
        sagaInstanceFactory: SagaInstanceFactory,
        createCommentSaga: CreateCommentSaga,
    ): NewsCastTicketService = NewsCastTicketService(sagaInstanceFactory, createCommentSaga)

    @Bean
    fun channelMapping(): ChannelMapping? = DefaultChannelMapping.builder().build()

    @Bean
    fun commandNameMapping(): CommandNameMapping = DefaultCommandNameMapping()

    @Bean
    fun duplicateMessageDetector(): DuplicateMessageDetector = NoopDuplicateMessageDetector()
}
