package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageRejectCommand
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
            .onMessage(NewsCastPageCommand::class.java, this::createPageComment)
            .onMessage(NewsCastPageRejectCommand::class.java, this::rejectPageComment)
            .build()
    }

    private fun createPageComment(commandPage: CommandMessage<NewsCastPageCommand>): Message {
        val command = commandPage.command
        val authorComment =
            newsCastPageCommentService.save(PageComment(
                pageId = command.idPage,
                comment = command.comment,
                requestId = command.requestId
            ))
        return withSuccess(authorComment)
    }

    private fun rejectPageComment(commandPage: CommandMessage<NewsCastPageRejectCommand>): Message {
        val command = commandPage.command
        val messageComment =
            command.requestId?.let { newsCastPageCommentService.getByRequestId(it) }
        messageComment?.copy(notAvailable = false)?.let { newsCastPageCommentService.save(it) }
            ?: CommandHandlerReplyBuilder.withFailure()
        return withSuccess(messageComment)
    }

}