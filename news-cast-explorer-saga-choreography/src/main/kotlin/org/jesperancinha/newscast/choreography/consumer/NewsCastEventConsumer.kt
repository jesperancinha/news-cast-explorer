package org.jesperancinha.newscast.choreography.consumer

import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KotlinLogging
import org.jesperancinha.newscast.choreography.event.NewsCastAuthorCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEventDone
import org.jesperancinha.newscast.choreography.event.NewsCastMessageCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastPageCommentEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments
import java.util.*

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastEventConsumer(private val domainEventPublisher: DomainEventPublisher? = null) {

    private val logger = KotlinLogging.logger {}

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
            .forAggregateType("org.jesperancinha.newscast.saga.data.NewsCastComments")
            .onEvent(NewsCastEvent::class.java, ::handleCreateNewsCastCommentEvent)
            .onEvent(NewsCastPageCommentEvent::class.java, ::handleCreatePageCommentEvent)
            .onEvent(NewsCastAuthorCommentEvent::class.java, ::handleCreateAuthorCommentEvent)
            .onEvent(NewsCastMessageCommentEvent::class.java, ::handleCreateMessageCommentEvent)
            .onEvent(NewsCastEventDone::class.java, ::handleDone)
            .build()
    }

    private fun handleCreateNewsCastCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastEvent>) {
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(),
            listOf(NewsCastPageCommentEvent(domainEventEnvelope.event.newsCastComments)))
    }

    private fun handleCreatePageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastPageCommentEvent>) {
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastAuthorCommentEvent(
            domainEventEnvelope.event.newsCastComments)))
    }

    private fun handleCreateAuthorCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastAuthorCommentEvent>) {
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastMessageCommentEvent(
            domainEventEnvelope.event.newsCastComments)))
    }

    private fun handleCreateMessageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastMessageCommentEvent>) {
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastEventDone(
            domainEventEnvelope.event.newsCastComments)))
    }

    fun handleDone(domainEventEnvelope: DomainEventEnvelope<NewsCastEventDone>) {
        logger.info("Saga is complete! ${domainEventEnvelope.event.newsCastComments}")
    }
}