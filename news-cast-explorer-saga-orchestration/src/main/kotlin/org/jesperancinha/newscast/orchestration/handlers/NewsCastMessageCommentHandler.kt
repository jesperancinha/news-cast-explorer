package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageRejectCommand
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.domain.MessageComment
import org.jesperancinha.newscast.service.MessageService


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastMessageCommentHandler(
    private val newsCastMessageCommentService: NewsCastMessageCommentService,
    private val messageService: MessageService
) {

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("messageChannel")
            .onMessage(NewsCastMessageCommand::class.java, this::createMessageComment)
            .onMessage(NewsCastMessageRejectCommand::class.java, this::rejectMessageComment)
            .build()
    }

    private fun createMessageComment(commandMessage: CommandMessage<NewsCastMessageCommand>): Message {
        val command = commandMessage.command
        val messageComment =
            newsCastMessageCommentService.save(MessageComment(
                messageId = command.idMessage,
                comment = command.comment,
                requestId = command.requestId
            ))
        return if (messageService.findMessageById(command.idMessage).isPresent)
            withSuccess(messageComment) else
            CommandHandlerReplyBuilder.withFailure()
    }

    private fun rejectMessageComment(commandMessage: CommandMessage<NewsCastMessageRejectCommand>): Message {
        val command = commandMessage.command
        val messageComment =
            command.requestId?.let {
                newsCastMessageCommentService.getByRequestId(it).let { messageComments ->
                    messageComments?.forEach { messageComment ->
                        newsCastMessageCommentService.save(messageComment.copy(notAvailable = false))

                    }
                }
            }
        return withSuccess(messageComment)
    }

}