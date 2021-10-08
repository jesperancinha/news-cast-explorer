package org.jesperancinha.newscast.choreography.consumer

import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import org.jesperancinha.newscast.choreography.event.NewsCastPageCommentEvent
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import org.jesperancinha.newscast.choreography.event.NewsCastAuthorCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEvent
import org.jesperancinha.newscast.choreography.event.NewsCastMessageCommentEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments
import java.util.*

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastEventConsumer(private val domainEventPublisher: DomainEventPublisher? = null) {
    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
            .forAggregateType("org.jesperancinha.newscast.saga.data.NewsCastComments")
            .onEvent(NewsCastEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<NewsCastEvent?>? ->
                handleCreateNewsCastCommentEvent(domainEventEnvelope)
            }
            .onEvent(NewsCastPageCommentEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<NewsCastPageCommentEvent?>? ->
                handleCreatePageCommentEvent(domainEventEnvelope)
            }
            .onEvent(NewsCastAuthorCommentEvent::class.java) { eDomainEventEnvelope: DomainEventEnvelope<NewsCastAuthorCommentEvent> ->
                handleCreateAuthorCommentEvent(eDomainEventEnvelope)
            }
            .onEvent(NewsCastMessageCommentEvent::class.java) { eDomainEventEnvelope: DomainEventEnvelope<NewsCastMessageCommentEvent> ->
                handleCreateMessageCommentEvent(eDomainEventEnvelope)
            }
            .build()
    }

    private fun handleCreateNewsCastCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastEvent?>?) {
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastPageCommentEvent(domainEventEnvelope?.event)))
    }

    private fun handleCreatePageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastPageCommentEvent?>?) {
    }

    private fun handleCreateAuthorCommentEvent(eDomainEventEnvelope: DomainEventEnvelope<NewsCastAuthorCommentEvent>) {

    }
    private fun handleCreateMessageCommentEvent(eDomainEventEnvelope: DomainEventEnvelope<NewsCastMessageCommentEvent>) {
    }
}