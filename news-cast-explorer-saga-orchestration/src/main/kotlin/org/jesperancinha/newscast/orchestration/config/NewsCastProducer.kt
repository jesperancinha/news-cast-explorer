package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.messaging.common.Message
import io.eventuate.tram.messaging.producer.MessageProducer
import org.springframework.stereotype.Component

/**
 * Created by jofisaes on 06/10/2021
 */
@Component
class NewsCastProducer:MessageProducer {
    override fun send(p0: String?, p1: Message?) {
        TODO("Not yet implemented")
    }
}