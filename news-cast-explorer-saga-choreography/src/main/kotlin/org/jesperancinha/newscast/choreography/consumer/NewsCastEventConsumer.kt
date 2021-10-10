package org.jesperancinha.newscast.choreography.consumer

import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KotlinLogging
import org.jesperancinha.newscast.choreography.event.NewsCastAuthorCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastAuthorRejectCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEvent
import org.jesperancinha.newscast.choreography.event.NewsCastEventDone
import org.jesperancinha.newscast.choreography.event.NewsCastMessageCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastPageCommentEvent
import org.jesperancinha.newscast.choreography.event.NewsCastPageRejectCommentEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments
import org.jesperancinha.newscast.saga.domain.AuthorComment
import org.jesperancinha.newscast.saga.domain.MessageComment
import org.jesperancinha.newscast.saga.domain.PageComment
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import java.util.*

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastEventConsumer(
    private val domainEventPublisher: DomainEventPublisher? = null,
    private val newsCasePageCommentService: NewsCastPageCommentService,
    private val newsCastAuthorCommentService: NewsCastAuthorCommentService,
    private val newsCastMessageCommentService: NewsCastMessageCommentService
    ) {

    private val logger = KotlinLogging.logger {}

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder
            .forAggregateType("org.jesperancinha.newscast.saga.data.NewsCastComments")
            .onEvent(NewsCastEvent::class.java, ::handleCreateNewsCastCommentEvent)
            .onEvent(NewsCastPageCommentEvent::class.java, ::handleCreatePageCommentEvent)
            .onEvent(NewsCastAuthorCommentEvent::class.java, ::handleCreateAuthorCommentEvent)
            .onEvent(NewsCastMessageCommentEvent::class.java, ::handleCreateMessageCommentEvent)
            .onEvent(NewsCastEventDone::class.java, ::handleDone)
            .onEvent(NewsCastPageRejectCommentEvent::class.java, ::handleRejectPageCommentEvent)
            .onEvent(NewsCastAuthorRejectCommentEvent::class.java, ::handleRejectAuthorCommentEvent)
            .onEvent(NewsCastMessageCommentEvent::class.java, ::handleRejectMessageCommentEvent)
            .build()
    }

    private fun handleCreateNewsCastCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(),
            listOf(NewsCastPageCommentEvent(newsCastComments)))
    }

    private fun handleCreatePageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastPageCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        val pageComment =
            newsCasePageCommentService.save(PageComment(
                pageId = newsCastComments.idPage,
                comment = newsCastComments.pageComment,
                requestId = newsCastComments.pageRequestId
            ))
        logger.info("Page comment registered $pageComment")
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastAuthorCommentEvent(
           newsCastComments)))
    }

    private fun handleCreateAuthorCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastAuthorCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        val authorComment =
            newsCastAuthorCommentService.save(AuthorComment(
                authorId = newsCastComments.idAuthor,
                comment = newsCastComments.authorComment,
                requestId = newsCastComments.authorRequestId
            ))
        logger.info("Author comment registered $authorComment")
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastMessageCommentEvent(
           newsCastComments)))
    }

    private fun handleCreateMessageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastMessageCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        val messageComment =
            newsCastMessageCommentService.save(MessageComment(
                messageId = newsCastComments.idMessage,
                comment = newsCastComments.messageComment,
                requestId = newsCastComments.messageRequestId
            ))
        logger.info("Message comment registered $messageComment")
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastEventDone(
           newsCastComments)))
    }

    private fun handleDone(domainEventEnvelope: DomainEventEnvelope<NewsCastEventDone>) {
        logger.info("Saga is complete! ${domainEventEnvelope.event.newsCastComments}")
    }

    private fun handleRejectPageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastPageRejectCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        newsCastComments.pageRequestId?.let {
            newsCasePageCommentService.getByRequestId(it)?.let { pageComments ->
                pageComments.forEach { pageComment ->
                    newsCasePageCommentService.save(pageComment.copy(notAvailable = true))
                }
            }
        }
    }

    private fun handleRejectAuthorCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastAuthorRejectCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        newsCastComments.authorRequestId?.let {
                newsCastAuthorCommentService.getByRequestId(it)?.let { authorComments ->
                    authorComments.forEach { authorComment ->
                        newsCastAuthorCommentService.save(authorComment.copy(notAvailable = true))
                    }
                }
            }
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(),
            listOf(NewsCastPageCommentEvent(newsCastComments)))    }

    private fun handleRejectMessageCommentEvent(domainEventEnvelope: DomainEventEnvelope<NewsCastMessageCommentEvent>) {
        val newsCastComments = domainEventEnvelope.event.newsCastComments
        newsCastComments.messageRequestId?.let {
            newsCastMessageCommentService.getByRequestId(it)?.let { messageComments ->
                messageComments.forEach { messageComment ->
                    newsCastMessageCommentService.save(messageComment.copy(notAvailable = true))
                }
            }
        }
        domainEventPublisher?.publish(NewsCastComments::class.java, UUID.randomUUID(), listOf(NewsCastAuthorCommentEvent(
           newsCastComments)))    }
}