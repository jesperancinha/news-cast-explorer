package org.jesperancinha.newscast.saga.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class MessageComment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val messageId: Long? = null,
    val requestId: Long? = null,
    val comment: String? = null,
    val notAvailable: Boolean = false
)