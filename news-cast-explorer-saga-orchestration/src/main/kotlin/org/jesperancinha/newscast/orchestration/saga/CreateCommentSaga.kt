package org.jesperancinha.newscast.orchestration.saga

import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorRejectCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageRejectCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageRejectCommand
import org.jesperancinha.newscast.saga.data.NewsCastComments
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository

/**
 * Created by jofisaes on 06/10/2021
 */
class CreateCommentSaga(
    val authorCommentRepository: AuthorCommentRepository,
) : SimpleSaga<NewsCastComments> {
    private val sagaDefinition = this.step()
        .invokeLocal(this::startSaga)
        .step()
        .invokeParticipant(this::recordPageComment)
        .withCompensation(this::rejectPageComment)
        .step()
        .invokeParticipant(this::recordAuthorComment)
        .withCompensation(this::rejectAuthorComment)
        .step()
        .invokeParticipant(this::recordMessageComment)
        .withCompensation(this::rejectMessageComment)
        .onReply(AuthorComment::class.java, this::didit)
        .step()
        .invokeLocal(this::done)
        .build()

    private fun recordPageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastPageCommand(
            idPage = createCommentSagaData.idPage,
            requestId = createCommentSagaData.pageRequestId,
            comment = createCommentSagaData.pageComment
        )).to("pageChannel").build()

    private fun recordAuthorComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastAuthorCommand(
            idAuthor = createCommentSagaData.idAuthor,
            requestId = createCommentSagaData.authorRequestId,
            comment = createCommentSagaData.authorComment
        )).to("authorChannel").build()

    private fun recordMessageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastMessageCommand(
            idMessage = createCommentSagaData.idMessage,
            requestId = createCommentSagaData.messageRequestId,
            comment = createCommentSagaData.messageComment
        )).to("messageChannel").build()

    private fun rejectPageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastPageRejectCommand(
            requestId = createCommentSagaData.pageRequestId
        )).to("pageChannel").build()

    private fun rejectAuthorComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastAuthorRejectCommand(
            requestId = createCommentSagaData.authorRequestId
        )).to("authorChannel").build()

    private fun rejectMessageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? =
        send(NewsCastMessageRejectCommand(
            requestId = createCommentSagaData.messageRequestId
        )).to("messageChannel").build()

    private fun startSaga(createCommentSagaData: NewsCastComments) {
    }

    private fun done(createCommentSagaData: NewsCastComments) {
        System.out.println("WOWOWOWOOWOWOW!!!!!!!!--------")
    }


    fun didit(saga: NewsCastComments, authorComment: AuthorComment?) {
        System.out.println("WOWOWOWOOWOWOW!!!!!!!!--------")
    }

    private fun createAuthorComment(createCommentSagaData: NewsCastComments) {
//        val authorComment = AuthorComment(
//            authorId = createCommentSagaData.idAuthor,
//            comment = createCommentSagaData.authorComment
//        )
//        val authorCommentRequest = authorCommentRepository.save(authorComment)
//        createCommentSagaData.authorRequestId = authorCommentRequest.id
    }

    private fun reject(createCommentSagaData: NewsCastComments) {

    }

    override fun getSagaDefinition(): SagaDefinition<NewsCastComments> {
        return sagaDefinition
    }
}
