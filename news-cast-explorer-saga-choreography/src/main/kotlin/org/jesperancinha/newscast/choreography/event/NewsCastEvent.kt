package org.jesperancinha.newscast.choreography.event

import io.eventuate.tram.events.common.DomainEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastEvent(newsCastComments: NewsCastComments) : DomainEvent {
}