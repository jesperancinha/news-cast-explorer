package org.jesperancinha.newscast

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.kafka.clients.producer.ProducerRecord
import org.jesperancinha.newscast.cdc.CdcConstants
import org.jesperancinha.newscast.cdc.KafkaCommand
import org.jesperancinha.newscast.cdc.KafkaProducerCreator
import org.jesperancinha.newscast.cdc.repository.MessageRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class CdcProcessLauncer(
    val messageRepository: MessageRepository,
) : CommandLineRunner {


    override fun run(vararg args: String?) {
        messageRepository.findAll().forEach {
            val objectMapper = ObjectMapper()
            val producer = KafkaProducerCreator.createProducer()
            val headers = objectMapper.readTree(it.headers)
            (headers as ObjectNode).put("command_type", "org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand")
            val command = KafkaCommand(it.payload, headers)
            val commandPayload = objectMapper.writeValueAsString(command)
            val record = ProducerRecord<Long?, String?>(CdcConstants.TOPIC_NAME, commandPayload)
            val metadata = producer.send(record).get();
            println("Sent: $commandPayload")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CdcProcessLauncer::class.java, *args)
}


