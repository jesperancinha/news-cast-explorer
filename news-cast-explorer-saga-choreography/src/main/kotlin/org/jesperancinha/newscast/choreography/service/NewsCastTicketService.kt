package org.jesperancinha.newscast.choreography.service

import io.eventuate.tram.events.publisher.DomainEventPublisher
import org.jesperancinha.newscast.choreography.event.NewsCastEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Created by jofisaes on 08/10/2021
 */
@Transactional
open class NewsCastTicketService(
    private val domainEventPublisher: DomainEventPublisher? = null,
) {
   open fun createNewsCastComments(newsCastComments: NewsCastComments): NewsCastComments {
        domainEventPublisher!!.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastEvent(newsCastComments)))
        return newsCastComments
    }
}