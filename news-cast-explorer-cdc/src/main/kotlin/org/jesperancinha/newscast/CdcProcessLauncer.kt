package org.jesperancinha.newscast

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.jesperancinha.newscast.cdc.KafkaCommand
import org.jesperancinha.newscast.cdc.KafkaProducerFactory
import org.jesperancinha.newscast.cdc.repository.MessageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@SpringBootApplication
@EnableScheduling
open class CdcProcessLauncer(
    private val messageRepository: MessageRepository,
    @Value("\${org.jesperancinha.newscast.host.kafka.brokers}")
    private val brokers: String
) {
    private val producer = KafkaProducerFactory.createProducer(brokers)


    @Scheduled(cron = "0/5 * * ? * *")
    fun fetchAndPublish() {
        messageRepository.findAllByPublishedIs(0).forEach {
            val objectMapper = ObjectMapper()
            val headers = objectMapper.readTree(it.headers)
            val command = KafkaCommand(it.payload, headers)
            val commandPayload = objectMapper.writeValueAsString(command)
            val record = ProducerRecord<Long?, String?>(it.destination, commandPayload)
            producer.send(record).get()
            messageRepository.save(it.copy(published = 1))
            println("Sent: $commandPayload")
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CdcProcessLauncer::class.java, *args)
        }
    }
}



