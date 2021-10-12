package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorRejectCommand
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.service.AuthorService


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastAuthorCommentHandler(
    private val newsCastAuthorCommentService: NewsCastAuthorCommentService,
    private val authorService: AuthorService,
) {

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("authorChannel")
            .onMessage(NewsCastAuthorCommand::class.java, this::createAuthorComment)
            .onMessage(NewsCastAuthorRejectCommand::class.java, this::rejectAuthorComment)
            .build()
    }

    private fun createAuthorComment(commandMessage: CommandMessage<NewsCastAuthorCommand>): Message {
        val command = commandMessage.command
        val authorComment =
            newsCastAuthorCommentService.save(AuthorComment(
                authorId = command.idAuthor,
                comment = command.comment,
                requestId = command.requestId
            ))
        return if (authorService.findAuthorById(command.idAuthor).isPresent)
            withSuccess(authorComment) else
            withFailure()
    }

    private fun rejectAuthorComment(commandMessage: CommandMessage<NewsCastAuthorRejectCommand>): Message {
        val command = commandMessage.command
        val authorComment =
            command.requestId?.let {
                newsCastAuthorCommentService.getByRequestId(it)?.let { authorComments ->
                    authorComments.forEach { authorComment ->
                        newsCastAuthorCommentService.save(authorComment.copy(notAvailable = true))
                    }
                }
            }

        return withSuccess(authorComment)
    }

}