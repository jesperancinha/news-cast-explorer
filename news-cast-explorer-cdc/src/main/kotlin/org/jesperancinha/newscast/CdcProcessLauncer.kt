package org.jesperancinha.newscast

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.kafka.clients.producer.ProducerRecord
import org.jesperancinha.newscast.cdc.KafkaCommand
import org.jesperancinha.newscast.cdc.KafkaProducerCreator
import org.jesperancinha.newscast.cdc.repository.MessageRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@SpringBootApplication
@EnableScheduling
open class CdcProcessLauncer(
    private val messageRepository: MessageRepository,
) {
    private val producer = KafkaProducerCreator.createProducer()

    @Scheduled(cron = "0/5 * * ? * *")
    fun fetchAndPublish() {
        messageRepository.findAllByPublishedIs(0).forEach {
            val objectMapper = ObjectMapper()
            val headers = objectMapper.readTree(it.headers)
            val command = KafkaCommand(it.payload, headers)
            val commandPayload = objectMapper.writeValueAsString(command)
            val record = ProducerRecord<Long?, String?>(it.destination, commandPayload)
            val metadata = producer.send(record).get()
            messageRepository.save(it.copy(published = 1))
            println("Sent: $commandPayload")
        }
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(CdcProcessLauncer::class.java, *args)
}


