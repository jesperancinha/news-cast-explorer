package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageCommand
import org.jesperancinha.newscast.orchestration.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.domain.MessageComment


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastMessageCommentHandler(
    private val newsCastMessageCommentService: NewsCastMessageCommentService,
) {

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("messageChannel")
            .onMessage(NewsCastMessageCommand::class.java, this::createMessageComment)
            .build()
    }

    private fun createMessageComment(commandMessage: CommandMessage<NewsCastMessageCommand>): Message {
        val command = commandMessage.command
        val authorComment =
            newsCastMessageCommentService.save(MessageComment(
                messageId = command.idMessage,
                comment = command.comment
            ))
        return withSuccess(authorComment)
    }

}