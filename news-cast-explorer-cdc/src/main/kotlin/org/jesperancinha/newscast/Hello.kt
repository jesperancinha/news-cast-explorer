package org.jesperancinha.newscast

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.jesperancinha.newscast.cdc.CdcConstants
import org.jesperancinha.newscast.cdc.KafkaCommand
import org.jesperancinha.newscast.cdc.KafkaProducerCreator
import org.jesperancinha.newscast.cdc.NewsCastAuthorCommand

fun main(args: Array<String>) {
    println("Hello, World")
    val objectMapper = ObjectMapper()
    val producer = KafkaProducerCreator.createProducer()
    val payload = objectMapper.writeValueAsString(NewsCastAuthorCommand(1L, 2L, "WOW"))
    val command = KafkaCommand(payload, mapOf(
        "ID" to "topopic",
        "command_type" to "org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand",
        "command_reply_to" to CdcConstants.TOPIC_NAME
    ))
    val commandPayload = objectMapper.writeValueAsString(command)
    val record = ProducerRecord<Long?, String?>(CdcConstants.TOPIC_NAME, commandPayload)
    val metadata = producer.send(record).get();
    println(metadata)
}


