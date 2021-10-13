package org.jesperancinha.newscast.cdc

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

/**
 * Created by jofisaes on 07/10/2021
 */
class KafkaProducerFactory {
    companion object {
        fun createProducer(brokers: String): Producer<Long?, String?> {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = brokers
            props[ProducerConfig.CLIENT_ID_CONFIG] = CdcConstants.CLIENT_ID
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            return KafkaProducer(props)
        }
    }
}