package org.jesperancinha.newscast.orchestration.handlers

import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure
import io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess
import io.eventuate.tram.commands.consumer.CommandHandlers
import io.eventuate.tram.commands.consumer.CommandMessage
import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder
import mu.KotlinLogging
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageRejectCommand
import org.jesperancinha.newscast.saga.domain.PageComment
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import org.jesperancinha.newscast.service.PageService


/**
 * Created by jofisaes on 06/10/2021
 */
class NewsCastPageCommentHandler(
    private val newsCastPageCommentService: NewsCastPageCommentService,
    private val pageService: PageService,
) {
    private val logger = KotlinLogging.logger {}

    fun commandHandlerDefinitions(): CommandHandlers {
        return SagaCommandHandlersBuilder
            .fromChannel("pageChannel")
            .onMessage(NewsCastPageCommand::class.java, this::createPageComment)
            .onMessage(NewsCastPageRejectCommand::class.java, this::rejectPageComment)
            .build()
    }

    private fun createPageComment(commandPage: CommandMessage<NewsCastPageCommand>): Message {
        val command = commandPage.command
        val pageComment = newsCastPageCommentService.save(PageComment(
            pageId = command.idPage,
            comment = command.comment,
            requestId = command.requestId
        ))
        return if (pageService.findPageById(command.idPage).isPresent) {
            withSuccess(pageComment)
        } else {
            makeNotAvailable(command.requestId)
            withFailure()
        }
    }

    private fun rejectPageComment(commandPage: CommandMessage<NewsCastPageRejectCommand>): Message {
        val requestId = commandPage.command.requestId
        return makeNotAvailable(requestId)
    }

    private fun makeNotAvailable(requestId: Long?): Message {
        return withSuccess(requestId?.let {
            newsCastPageCommentService.getByRequestId(it).let { pageComments ->
                pageComments?.forEach { pageComment ->
                    newsCastPageCommentService.save(pageComment.copy(notAvailable = true))
                }
            }
        })
    }
}