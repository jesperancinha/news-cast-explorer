package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
import org.jesperancinha.newscast.orchestration.service.NewsCastPageCommentService
import org.jesperancinha.newscast.saga.domain.PageComment


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastPageCommentHandler(
    private val newsCastPageCommentService: NewsCastPageCommentService,
) {
    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("pageChannel")
            .onMessage(NewsCastPageCommand::class.java, this::createMessageComment)
            .build()
    }

    private fun createMessageComment(commandMessage: CommandMessage<NewsCastPageCommand>): Message {
        val command = commandMessage.command
        val authorComment =
            newsCastPageCommentService.save(PageComment(
                pageId = command.idPage,
                comment = command.comment
            ))
        return withSuccess(authorComment)
    }

}