package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.commands.consumer.CommandDispatcher
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration
import org.jesperancinha.newscast.orchestration.handlers.NewsCastAuthorCommentHandler
import org.jesperancinha.newscast.orchestration.service.NewsCastAuthorCommentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import


/**
 * Created by jofisaes on 06/10/2021
 */
@Configuration
@Import(SagaParticipantConfiguration::class)
class NewsCastOrchestrationConfiguration {
    @Bean
    fun newsCastAuthorCommentHandler(newsCastAuthorCommentService: NewsCastAuthorCommentService): NewsCastAuthorCommentHandler {
        return NewsCastAuthorCommentHandler(newsCastAuthorCommentService)
    }

    @Bean
    fun consumerCommandDispatcher(
        target: NewsCastAuthorCommentHandler,
        sagaCommandDispatcherFactory: SagaCommandDispatcherFactory,
    ): CommandDispatcher {
        return sagaCommandDispatcherFactory
            .make("customerCommandDispatcher", target.commandHandlerDefinitions())
    }
}