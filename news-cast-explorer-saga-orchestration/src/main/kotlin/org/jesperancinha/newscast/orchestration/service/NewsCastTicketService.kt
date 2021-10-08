package org.jesperancinha.newscast.orchestration.service

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.saga.data.NewsCastComments
import org.springframework.transaction.annotation.Transactional


/**
 * Created by jofisaes on 06/10/2021
 */
open class NewsCastTicketService(
    private val sagaInstanceFactory: SagaInstanceFactory,
    private val createCommentSaga: CreateCommentSaga,
) {
    @Transactional
    open fun createOrder(newsCastComments: NewsCastComments): String {
        val sagaInstance = sagaInstanceFactory.create(createCommentSaga, newsCastComments)
        return sagaInstance.id
    }
}