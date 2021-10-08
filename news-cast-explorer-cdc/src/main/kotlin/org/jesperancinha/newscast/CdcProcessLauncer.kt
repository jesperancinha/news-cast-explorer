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
    val messageRepository: MessageRepository,
) {

    @Scheduled(cron = "0 * * * * *")
    fun fetchAndPublish() {
        messageRepository.findAllByPublishedIs(0).forEach {
            val objectMapper = ObjectMapper()
            val producer = KafkaProducerCreator.createProducer()
            val headers = objectMapper.readTree(it.headers)
            (headers as ObjectNode).put("command_type",
                "org.jesperancinha.newscast.orchestration.commands.NewsCastAuthorCommand")
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


