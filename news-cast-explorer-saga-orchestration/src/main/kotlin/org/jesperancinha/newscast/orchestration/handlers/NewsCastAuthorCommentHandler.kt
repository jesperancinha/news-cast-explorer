package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.orchestration.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.domain.AuthorComment


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastAuthorCommentHandler(
    val newsCastAuthorCommentService: NewsCastAuthorCommentService,
) {

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("newsCastAuthorCommentService")
            .onMessage(NewsCastAuthorCommand::class.java, this::createAuthorComment)
            .build()
    }

    private fun createAuthorComment(commandMessage: CommandMessage<NewsCastAuthorCommand>): Message {
        val command = commandMessage.command
        val authorComment =
        newsCastAuthorCommentService.save(AuthorComment(
            authorId= command.idAuthor,
            comment = command.authorComment
        ))
        return withSuccess(authorComment)
    }

}