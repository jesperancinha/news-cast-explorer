package org.jesperancinha.newscast.choreography.event

import io.eventuate.tram.events.common.DomainEvent
import org.jesperancinha.newscast.saga.data.NewsCastComments

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastAuthorCommentEvent(
    val newsCastComments: NewsCastComments = NewsCastComments()
) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastAuthorRejectCommentEvent(
    val newsCastComments: NewsCastComments = NewsCastComments()
) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastDoneEvent(val newsCastComments: NewsCastComments? = null) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastEvent(val newsCastComments: NewsCastComments = NewsCastComments()) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastMessageCommentEvent(val newsCastComments: NewsCastComments = NewsCastComments()) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastMessageRejectCommentEvent(val newsCastComments: NewsCastComments = NewsCastComments()) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastPageCommentEvent(
    val newsCastComments: NewsCastComments = NewsCastComments()
) : DomainEvent

/**
 * Created by jofisaes on 08/10/2021
 */
data class NewsCastPageRejectCommentEvent(
    val newsCastComments: NewsCastComments = NewsCastComments()
) : DomainEvent