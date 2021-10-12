package org.jesperancinha.newscast.saga.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

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