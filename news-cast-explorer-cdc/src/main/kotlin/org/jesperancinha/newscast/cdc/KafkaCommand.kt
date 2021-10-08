package org.jesperancinha.newscast.cdc

/**
 * Created by jofisaes on 08/10/2021
 */
data class KafkaCommand(
    val payload: String? = null,
    val headers: Map<String, String>? = null,
) {
    constructor() : this(null, null)
}