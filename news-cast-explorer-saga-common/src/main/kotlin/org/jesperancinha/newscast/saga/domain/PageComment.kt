package org.jesperancinha.newscast.saga.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class PageComment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null,
    val pageId: Long? = null,
    val requestId: Long? = null,
    val comment: String? = null,
    val notAvailable: Boolean = false,
) {
    constructor() : this(null, null, null, null)
}