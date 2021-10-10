package org.jesperancinha.newscast.choreography.config

import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import org.jesperancinha.newscast.choreography.consumer.NewsCastEventConsumer
import org.jesperancinha.newscast.choreography.service.NewsCastTicketService
import org.jesperancinha.newscast.saga.service.NewsCastAuthorCommentService
import org.jesperancinha.newscast.saga.service.NewsCastMessageCommentService
import org.jesperancinha.newscast.saga.service.NewsCastPageCommentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@Import(
    TramJdbcKafkaConfiguration::class, TramEventsPublisherConfiguration::class, TramEventSubscriberConfiguration::class)
open class NewsCastChoreographyConfiguration {
    @Bean
    open fun orderEventConsumer(
        domainEventPublisher: DomainEventPublisher,
        newsCasePageCommentService: NewsCastPageCommentService,
        newsCastAuthorCommentService: NewsCastAuthorCommentService,
        newsCastMessageCommentService: NewsCastMessageCommentService,
    ): NewsCastEventConsumer {
        return NewsCastEventConsumer(domainEventPublisher,
            newsCasePageCommentService,
            newsCastAuthorCommentService,
            newsCastMessageCommentService)
    }

    @Bean
    open fun domainEventDispatcher(
        orderEventConsumer: NewsCastEventConsumer,
        domainEventDispatcherFactory: DomainEventDispatcherFactory,
    ): DomainEventDispatcher {
        return domainEventDispatcherFactory.make(
            "newsCastAuthorCommentDispatcher", orderEventConsumer.domainEventHandlers())
    }

    @Bean
    open fun orderService(domainEventPublisher: DomainEventPublisher?): NewsCastTicketService {
        return NewsCastTicketService(domainEventPublisher)
    }
}