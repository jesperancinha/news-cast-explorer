package org.jesperancinha.newscast.orchestration.saga

import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastMessageCommand
import org.jesperancinha.newscast.orchestration.commands.NewsCastPageCommand
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
//        .invokeLocal { createCommentSagaData: CreateCommentSagaData -> createAuthorComment(createCommentSagaData) }
//        .withCompensation { createCommentSagaData: CreateCommentSagaData -> reject(createCommentSagaData) }
//        .step()
        .invokeLocal(this::approve)
        .step()
        .invokeLocal(this::approve)
        .step()
        .invokeParticipant(this::recordPageComment)
        .step()
        .invokeParticipant(this::recordAuthorComment)
        .step()
        .invokeParticipant(this::recordMessageComment)
        .withCompensation(this::reject1)
        .onReply(AuthorComment::class.java, this::didit)
        .step()
        .invokeLocal(this::done)
        .build()

    private fun recordPageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? {
        return send(NewsCastPageCommand(
            idPage = createCommentSagaData.idPage,
            requestId = createCommentSagaData.pageRequestId,
            comment = createCommentSagaData.pageComment
        )).to("pageChannel").build()    }

    private fun recordAuthorComment(createCommentSagaData: NewsCastComments): CommandWithDestination? {
        return send(NewsCastAuthorCommand(
            idAuthor = createCommentSagaData.idAuthor,
            requestId = createCommentSagaData.authorRequestId,
            comment = createCommentSagaData.authorComment
        )).to("authorChannel").build()
    }

    private fun recordMessageComment(createCommentSagaData: NewsCastComments): CommandWithDestination? {
        return send(NewsCastMessageCommand(
            idMessage = createCommentSagaData.idMessage,
            requestId = createCommentSagaData.messageRequestId,
            comment = createCommentSagaData.messageComment
        )).to("messageChannel").build()    }

    private fun reject1(createCommentSagaData: NewsCastComments): CommandWithDestination? {
        TODO("Not yet implemented")
    }

    private fun approve(createCommentSagaData: NewsCastComments) {
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
