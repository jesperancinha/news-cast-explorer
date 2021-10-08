package org.jesperancinha.newscast.cdc

import com.fasterxml.jackson.databind.JsonNode

/**
 * Created by jofisaes on 08/10/2021
 */
data class KafkaCommand(
    val payload: String? = null,
    val headers: JsonNode? = null,
) {
    constructor() : this(null, null)
}