package org.jesperancinha.newscast.cdc.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Created by jofisaes on 08/10/2021
 */
@Entity
@Table(name = "message", schema = "eventuate")
data class Message(
    @Id
    val id: String? = null,
    val destination: String? = null,
    val headers: String? = null,
    val payload: String? = null,
    val creation_time: Long? = null,
    val published: Int? = null
) {
    constructor() : this(null, null, null, null, null, null)

}