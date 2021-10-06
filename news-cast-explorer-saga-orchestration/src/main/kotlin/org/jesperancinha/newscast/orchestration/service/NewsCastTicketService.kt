package org.jesperancinha.newscast.orchestration.service

import io.eventuate.tram.sagas.orchestration.SagaInstanceFactory
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga
import org.jesperancinha.newscast.orchestration.saga.CreateCommentSagaData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by jofisaes on 06/10/2021
 */
@Service
open class NewsCastTicketService(
    @Autowired
    val sagaInstanceFactory: SagaInstanceFactory,
    @Autowired
    val createCommentSaga: CreateCommentSaga,
) {
    @Transactional
    fun createOrder(orderDetails: CreateCommentSagaData): String {
        val sagaInstance = sagaInstanceFactory.create(createCommentSaga, orderDetails)
        return sagaInstance.id
    }
}