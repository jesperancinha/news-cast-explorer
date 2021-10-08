package org.jesperancinha.newscast.choreography.event

import io.eventuate.tram.events.common.DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
class NewsCastPageCommentEvent(
    val event: NewsCastEvent? = null,
) : DomainEvent {
    constructor() : this(null)
}