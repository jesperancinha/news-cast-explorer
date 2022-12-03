package org.jesperancinha.newscast.choreography.config

import io.eventuate.tram.commands.common.CommandNameMapping
import io.eventuate.tram.commands.common.DefaultCommandNameMapping
import io.eventuate.tram.consumer.common.DuplicateMessageDetector
import io.eventuate.tram.consumer.common.NoopDuplicateMessageDetector
import io.eventuate.tram.events.common.DefaultDomainEventNameMapping
import io.eventuate.tram.events.common.DomainEventNameMapping
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.messaging.common.ChannelMapping
import io.eventuate.tram.messaging.common.DefaultChannelMapping
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import org.jesperancinha.newscast.choreography.consumer.NewsCastEventConsumer
import org.jesperancinha.newscast.choreography.service.NewsCastTicketService
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import org.jesperancinha.newscast.service.AuthorService
import org.jesperancinha.newscast.service.MessageService
import org.jesperancinha.newscast.service.PageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    TramJdbcKafkaConfiguration::class, TramEventsPublisherConfiguration::class, TramEventSubscriberConfiguration::class
)
class NewsCastChoreographyConfiguration {
    @Bean
    fun orderEventConsumer(
        domainEventPublisher: DomainEventPublisher,
        newsCasePageCommentService: NewsCastPageCommentService,
        newsCastAuthorCommentService: NewsCastAuthorCommentService,
        newsCastMessageCommentService: NewsCastMessageCommentService,
        pageService: PageService,
        authorService: AuthorService,
        messageService: MessageService,
    ): NewsCastEventConsumer = NewsCastEventConsumer(
        domainEventPublisher,
        newsCasePageCommentService,
        newsCastAuthorCommentService,
        newsCastMessageCommentService,
        pageService,
        authorService,
        messageService
    )

    @Bean
    fun domainEventDispatcher(
        orderEventConsumer: NewsCastEventConsumer,
        domainEventDispatcherFactory: DomainEventDispatcherFactory,
    ): DomainEventDispatcher = domainEventDispatcherFactory.make(
        "newsCastAuthorCommentDispatcher", orderEventConsumer.domainEventHandlers()
    )

    @Bean
    fun orderService(domainEventPublisher: DomainEventPublisher?) = NewsCastTicketService(domainEventPublisher)

    @Bean
    fun channelMapping(): ChannelMapping? = DefaultChannelMapping.builder().build()

    @Bean
    fun commandNameMapping(): CommandNameMapping = DefaultCommandNameMapping()

    @Bean
    fun duplicateMessageDetector(): DuplicateMessageDetector = NoopDuplicateMessageDetector()

    @Bean
    fun domainEventNameMapping(): DomainEventNameMapping = DefaultDomainEventNameMapping()
}