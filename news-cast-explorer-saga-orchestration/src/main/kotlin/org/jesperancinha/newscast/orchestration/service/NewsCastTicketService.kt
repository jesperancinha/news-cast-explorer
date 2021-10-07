package org.jesperancinha.newscast.orchestration.service

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import io.eventuate.tram.sagas.orchestration.SagaManagerImpl
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSagaData
import org.springframework.transaction.annotation.Transactional


/**
 * Created by jofisaes on 06/10/2021
 */
open class NewsCastTicketService(
    private val sagaInstanceFactory: SagaInstanceFactory,
    private val createCommentSaga: CreateCommentSaga,
) {
    @Transactional
    open fun createOrder(orderDetails: CreateCommentSagaData): String {
        val sagaInstance = sagaInstanceFactory.create(createCommentSaga, orderDetails)
        return sagaInstance.id
    }
}