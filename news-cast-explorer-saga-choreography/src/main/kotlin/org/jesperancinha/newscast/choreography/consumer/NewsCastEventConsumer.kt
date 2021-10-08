package org.jesperancinha.newscast.choreography.consumer

import org.springframework.beans.factory.annotation.Autowired
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import org.jesperancinha.newscast.choreography.event.NewsCastCommentCreatedEvent
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import org.jesperancinha.newscast.choreography.event.NewsCastCommentCancelledEvent
import io.eventuate.tram.events.common.DomainEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEvent

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastEventConsumer(val domainEventPublisher: DomainEventPublisher? = null) {

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
            .forAggregateType("org.jesperancinha.newscast.saga.data.NewsCastComments")
            .onEvent(NewsCastEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<NewsCastEvent?>? ->
                handleCreateNewsCastCommentEvent(domainEventEnvelope)
            }
            .onEvent(NewsCastCommentCreatedEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<NewsCastCommentCreatedEvent?>? ->
                handleOrderCreatedEventHandler(domainEventEnvelope)
            }
            .onEvent(NewsCastCommentCancelledEvent::class.java) { eDomainEventEnvelope: DomainEventEnvelope<NewsCastCommentCancelledEvent> ->
                handleOrderCancelledEvent(eDomainEventEnvelope)
            }
            .build()
    }

    private fun handleCreateNewsCastCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastEvent?>?) {
    }

    private fun <E : DomainEvent?> handleOrderCancelledEvent(eDomainEventEnvelope: DomainEventEnvelope<E>) {

    }
    fun handleOrderCreatedEventHandler(
        domainEventEnvelope: DomainEventEnvelope<NewsCastCommentCreatedEvent?>?
    ) {
    }
}