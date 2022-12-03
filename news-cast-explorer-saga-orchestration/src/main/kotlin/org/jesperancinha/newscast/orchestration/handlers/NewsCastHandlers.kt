package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import org.jesperancinha.newscast.orchestration.commands.*
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.domain.MessageComment
import org.jesperancinha.newscast.saga.domain.PageComment
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import org.jesperancinha.newscast.service.AuthorService
import org.jesperancinha.newscast.service.MessageService
import org.jesperancinha.newscast.service.PageService

/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastAuthorCommentHandler(
    private val newsCastAuthorCommentService: NewsCastAuthorCommentService,
    private val authorService: AuthorService,
) {

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder.fromChannel("authorChannel")
            .onMessage(NewsCastAuthorCommand::class.java, this::createAuthorComment)
            .onMessage(NewsCastAuthorRejectCommand::class.java, this::rejectAuthorComment)
            .build()
    }

    private fun createAuthorComment(commandMessage: CommandMessage<NewsCastAuthorCommand>): Message {
        val command = commandMessage.command
        val authorComment = newsCastAuthorCommentService.save(
            AuthorComment(
                authorId = command.idAuthor,
                comment = command.comment,
                requestId = command.requestId
            )
        )
        return if (authorService.findAuthorById(command.idAuthor).isPresent) {
            CommandHandlerReplyBuilder.withSuccess(authorComment)
        } else {
            makeNotAvailable(command.requestId)
            CommandHandlerReplyBuilder.withFailure()
        }
    }

    private fun rejectAuthorComment(commandMessage: CommandMessage<NewsCastAuthorRejectCommand>): Message {
        val requestId = commandMessage.command.requestId
        return makeNotAvailable(requestId)
    }

    private fun makeNotAvailable(requestId: Long?): Message {
        return CommandHandlerReplyBuilder.withSuccess(requestId?.let {
            newsCastAuthorCommentService.getByRequestId(it)?.let { authorComments ->
                authorComments.forEach { authorComment ->
                    newsCastAuthorCommentService.save(authorComment.copy(notAvailable = true))
                }
            }
        })
    }
}

/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastMessageCommentHandler(
    private val newsCastMessageCommentService: NewsCastMessageCommentService,
    private val messageService: MessageService,
) {

    fun commandHandlerDefinitions(): CommandHandlers = SagaCommandHandlersBuilder.fromChannel("messageChannel")
        .onMessage(NewsCastMessageCommand::class.java, this::createMessageComment)
        .onMessage(NewsCastMessageRejectCommand::class.java, this::rejectMessageComment)
        .build()

    private fun createMessageComment(commandMessage: CommandMessage<NewsCastMessageCommand>): Message {
        val command = commandMessage.command
        val messageComment = newsCastMessageCommentService.save(
            MessageComment(
                messageId = command.idMessage,
                comment = command.comment,
                requestId = command.requestId
            )
        )
        return if (messageService.findMessageById(command.idMessage).isPresent) {
            CommandHandlerReplyBuilder.withSuccess(messageComment)
        } else {
            makeNotAvailable(command.requestId)
            CommandHandlerReplyBuilder.withFailure()
        }
    }

    private fun rejectMessageComment(commandMessage: CommandMessage<NewsCastMessageRejectCommand>): Message =
        makeNotAvailable(commandMessage.command.requestId)

    private fun makeNotAvailable(requestId: Long?): Message {
        return CommandHandlerReplyBuilder.withSuccess(requestId?.let {
            newsCastMessageCommentService.getByRequestId(it).let { messageComments ->
                messageComments?.forEach { messageComment ->
                    newsCastMessageCommentService.save(messageComment.copy(notAvailable = true))
                }
            }
        })
    }

}

/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastPageCommentHandler(
    private val newsCastPageCommentService: NewsCastPageCommentService,
    private val pageService: PageService,
) {

    fun commandHandlerDefinitions(): CommandHandlers = SagaCommandHandlersBuilder.fromChannel("pageChannel")
        .onMessage(NewsCastPageCommand::class.java, this::createPageComment)
        .onMessage(NewsCastPageRejectCommand::class.java, this::rejectPageComment)
        .build()

    private fun createPageComment(commandPage: CommandMessage<NewsCastPageCommand>): Message {
        val command = commandPage.command
        val pageComment = newsCastPageCommentService.save(
            PageComment(
                pageId = command.idPage,
                comment = command.comment,
                requestId = command.requestId
            )
        )
        return if (pageService.findPageById(command.idPage).isPresent) {
            CommandHandlerReplyBuilder.withSuccess(pageComment)
        } else {
            makeNotAvailable(command.requestId)
            CommandHandlerReplyBuilder.withFailure()
        }
    }

    private fun rejectPageComment(commandPage: CommandMessage<NewsCastPageRejectCommand>): Message {
        val requestId = commandPage.command.requestId
        return makeNotAvailable(requestId)
    }

    private fun makeNotAvailable(requestId: Long?): Message {
        return CommandHandlerReplyBuilder.withSuccess(requestId?.let {
            newsCastPageCommentService.getByRequestId(it).let { pageComments ->
                pageComments?.forEach { pageComment ->
                    newsCastPageCommentService.save(pageComment.copy(notAvailable = true))
                }
            }
        })
    }
}