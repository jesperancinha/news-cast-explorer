package org.jesperancinha.newscast.orchestration.config

import io.eventuate.tram.messaging.consumer.MessageConsumer
import io.eventuate.tram.messaging.consumer.MessageHandler
import io.eventuate.tram.messaging.consumer.MessageSubscription
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by jofisaes on 06/10/2021
 */
@Component
class NewsCastConsumer:MessageConsumer {
    override fun subscribe(p0: String?, p1: MutableSet<String>?, p2: MessageHandler?): MessageSubscription {
        return MessageSubscription {  }
    }

    override fun getId(): String = UUID.randomUUID().toString()

    override fun close() {
    }
}