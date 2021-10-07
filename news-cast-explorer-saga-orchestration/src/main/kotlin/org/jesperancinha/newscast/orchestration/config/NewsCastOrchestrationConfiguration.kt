package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.commands.consumer.CommandDispatcher
import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import org.jesperancinha.newscast.orchestration.handlers.NewsCastAuthorCommentHandler
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.orchestration.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.orchestration.service.NewsCastTicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


/**
 * Created by jofisaes on 06/10/2021
 */
@Configuration
@Import(SagaParticipantConfiguration::class)
open class NewsCastOrchestrationConfiguration(
    @Autowired
    val sagaInstanceFactory: SagaInstanceFactory,
    @Autowired
    val createCommentSaga: CreateCommentSaga,
) {
    @Bean
    open fun newsCastAuthorCommentHandler(newsCastAuthorCommentService: NewsCastAuthorCommentService): NewsCastAuthorCommentHandler {
        return NewsCastAuthorCommentHandler(newsCastAuthorCommentService)
    }

    @Bean
    open fun consumerCommandDispatcher(
        target: NewsCastAuthorCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): CommandDispatcher {
        return sagaCommandDispatcherFactory
            .make("customerCommandDispatcher", target.commandHandlerDefinitions())
    }

    @Bean
    open fun newsCastTicketService(
        sagaInstanceFactory: SagaInstanceFactory,
        createCommentSaga: CreateCommentSaga,
    ): NewsCastTicketService {
        return NewsCastTicketService(sagaInstanceFactory, createCommentSaga)
    }
}
