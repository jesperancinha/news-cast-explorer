package org.jesperancinha.newscast.orchestration.saga

import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga
import org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.repository.AuthorCommentRepository

/**
 * Created by jofisaes on 06/10/2021
 */
class CreateCommentSaga(
    val authorCommentRepository: AuthorCommentRepository,
) : SimpleSaga<CreateCommentSagaData> {
    private val sagaDefinition = this.step()
//        .invokeLocal { createCommentSagaData: CreateCommentSagaData -> createAuthorComment(createCommentSagaData) }
//        .withCompensation { createCommentSagaData: CreateCommentSagaData -> reject(createCommentSagaData) }
//        .step()
        .invokeLocal(this::approve)
        .step()
        .invokeLocal(this::approve)
        .step()
        .invokeParticipant(this::recordAuthorComment)
        .step()
        .invokeLocal(this::approve)
        .build()

    private fun approve(createCommentSagaData: CreateCommentSagaData) {
    }

    fun didit(saga:CreateCommentSagaData,authorComment: AuthorComment?) {
        System.out.println("WOWOWOWOOWOWOW!!!!!!!!--------")
    }


    private fun createAuthorComment(createCommentSagaData: CreateCommentSagaData) {
//        val authorComment = AuthorComment(
//            authorId = createCommentSagaData.idAuthor,
//            comment = createCommentSagaData.authorComment
//        )
//        val authorCommentRequest = authorCommentRepository.save(authorComment)
//        createCommentSagaData.authorRequestId = authorCommentRequest.id
    }

    private fun recordAuthorComment(createCommentSagaData: CreateCommentSagaData): CommandWithDestination? {
        return send(NewsCastAuthorCommand(
            idAuthor = createCommentSagaData.idAuthor,
            requestId = createCommentSagaData.authorRequestId,
            comment = createCommentSagaData.authorComment
        )).to("authorChannel").build()
    }

    private fun reject(createCommentSagaData: CreateCommentSagaData) {

    }

    override fun getSagaDefinition(): SagaDefinition<CreateCommentSagaData> {
        return sagaDefinition
    }
}
