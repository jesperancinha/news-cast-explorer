package org.jesperancinha.newscast.orchestration.saga

import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga
import mu.KotlinLogging
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorRejectCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageRejectCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageRejectCommand
import org.jesperancinha.newscast.saga.data.NewsCastComments
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.domain.MessageComment
import org.jesperancinha.newscast.saga.domain.PageComment

/**
 * Created by jofisaes on 06/10/2021
 */
class CreateCommentSaga : SimpleSaga<NewsCastComments> {
    private val logger = KotlinLogging.logger {}

    private val sagaDefinition = this.step()
        .invokeLocal(this::startSaga)
        .step()
        .invokeParticipant(this::recordPageComment)
        .onReply(PageComment::class.java, this::savedPageComent)
        .withCompensation(this::rejectPageComment)
        .onReply(PageComment::class.java, this::rejectedPageComment)
        .step()
        .invokeParticipant(this::recordAuthorComment)
        .onReply(AuthorComment::class.java, this::savedAuthorComment)
        .withCompensation(this::rejectAuthorComment)
        .onReply(AuthorComment::class.java, this::rejectedAuthorComment)
        .step()
        .invokeParticipant(this::recordMessageComment)
        .onReply(MessageComment::class.java, this::savedMessageComment)
        .withCompensation(this::rejectMessageComment)
        .onReply(MessageComment::class.java, this::rejectedMessageComment)
        .step()
        .invokeLocal(this::done)
        .build()

    private fun startSaga(newsCastComments: NewsCastComments) = logger.info("Saga has started: $newsCastComments")

    private fun recordPageComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastPageCommand(
            idPage = newsCastComments.idPage,
            requestId = newsCastComments.pageRequestId,
            comment = newsCastComments.pageComment
        )).to("pageChannel").build()

    private fun savedPageComent(newsCastComments: NewsCastComments, pageComment: PageComment?) {
        logger.info("Page comment is registered: $pageComment")
    }


    private fun rejectPageComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastPageRejectCommand(
            requestId = newsCastComments.pageRequestId
        )).to("pageChannel").build()

    private fun rejectedPageComment(newsCastComments: NewsCastComments, pageComment: PageComment?) {
        logger.info("Page comment is rejected: $pageComment")
    }

    private fun recordAuthorComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastAuthorCommand(
            idAuthor = newsCastComments.idAuthor,
            requestId = newsCastComments.authorRequestId,
            comment = newsCastComments.authorComment
        )).to("authorChannel").build()

    fun savedAuthorComment(newsCastComments: NewsCastComments, authorComment: AuthorComment?) {
        logger.info("Author comment is registered: $authorComment")
    }

    private fun rejectAuthorComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastAuthorRejectCommand(
            requestId = newsCastComments.authorRequestId
        )).to("authorChannel").build()

    private fun rejectedAuthorComment(newsCastComments: NewsCastComments, authorComment: AuthorComment?) {
        logger.info("Author comment is rejected: $authorComment")
    }

    private fun recordMessageComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastMessageCommand(
            idMessage = newsCastComments.idMessage,
            requestId = newsCastComments.messageRequestId,
            comment = newsCastComments.messageComment
        )).to("messageChannel").build()

    private fun savedMessageComment(newsCastComments: NewsCastComments, messageComment: MessageComment?) {
        logger.info("Message comment is registered: $messageComment")
    }

    private fun rejectMessageComment(newsCastComments: NewsCastComments): CommandWithDestination? =
        send(NewsCastMessageRejectCommand(
            requestId = newsCastComments.messageRequestId
        )).to("messageChannel").build()

    private fun rejectedMessageComment(saga: NewsCastComments, messageComment: MessageComment?) {
        logger.info("Message comment is rejected: $messageComment")
    }

    private fun done(newsCastComments: NewsCastComments) {
        logger.info("Saga is completed: $newsCastComments")
    }

    override fun getSagaDefinition(): SagaDefinition<NewsCastComments> {
        return sagaDefinition
    }
}
