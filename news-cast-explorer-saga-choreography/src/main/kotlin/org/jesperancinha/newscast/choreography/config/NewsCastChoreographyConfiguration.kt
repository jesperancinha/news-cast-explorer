package org.jesperancinha.newscast.choreography.config

import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import org.jesperancinha.newscast.choreography.consumer.NewsCastEventConsumer
import org.jesperancinha.newscast.choreography.service.NewsCastTicketService
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import(
    TramJdbcKafkaConfiguration::class, TramEventsPublisherConfiguration::class, TramEventSubscriberConfiguration::class)
open class NewsCastChoreographyConfiguration {
    @Bean
    open fun orderEventConsumer(domainEventPublisher: DomainEventPublisher): NewsCastEventConsumer {
        return NewsCastEventConsumer(domainEventPublisher)
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