package org.jesperancinha.newscast.orchestration.saga

import io.eventuate.tram.commands.consumer.CommandWithDestination
import io.eventuate.tram.sagas.orchestration.SagaDefinition
import io.eventuate.tram.sagas.simpledsl.SimpleSaga

/**
 * Created by jofisaes on 06/10/2021
 */
class CreateCommentSaga : SimpleSaga<CreateCommentSagaData> {
    private val sagaDefinition = step()
        .invokeLocal { createCommentSagaData: CreateCommentSagaData -> create(createCommentSagaData) }
        .withCompensation { createCommentSagaData: CreateCommentSagaData -> reject(createCommentSagaData) }
        .step()
        .invokeParticipant { createCommentSagaData: CreateCommentSagaData -> reserve(createCommentSagaData) }
        .build()

    private fun reserve(createCommentSagaData: CreateCommentSagaData): CommandWithDestination? {
        return null
    }

    private fun reject(createCommentSagaData: CreateCommentSagaData) {}
    private fun create(createCommentSagaData: CreateCommentSagaData) {}
    override fun getSagaDefinition(): SagaDefinition<CreateCommentSagaData> {
        return sagaDefinition
    }
}