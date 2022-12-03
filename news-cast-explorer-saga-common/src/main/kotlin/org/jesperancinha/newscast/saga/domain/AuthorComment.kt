package org.jesperancinha.newscast.saga.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class AuthorComment(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val authorId: Long? = null,
    val requestId: Long? = null,
    val comment: String? = null,
    val notAvailable: Boolean = false
)