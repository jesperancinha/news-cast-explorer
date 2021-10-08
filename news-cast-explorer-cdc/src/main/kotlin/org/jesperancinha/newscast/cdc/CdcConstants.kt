package org.jesperancinha.newscast.cdc

/**
 * Created by jofisaes on 07/10/2021
 */
class CdcConstants {
    companion object {
        const val KAFKA_BROKERS = "localhost:9092"
        const val MESSAGE_COUNT = 1000
        const val CLIENT_ID = "client1"
//        const val TOPIC_NAME = "org.jesperancinha.newscast.orchestration.saga.CreateCommentSaga-reply"
        const val TOPIC_NAME = "authorChannel"
        const val GROUP_ID_CONFIG = "consumerGroup1"
        const val MAX_NO_MESSAGE_FOUND_COUNT = 100
        const val OFFSET_RESET_LATEST = "latest"
        const val OFFSET_RESET_EARLIER = "earliest"
        const val MAX_POLL_RECORDS = 1
    }
}