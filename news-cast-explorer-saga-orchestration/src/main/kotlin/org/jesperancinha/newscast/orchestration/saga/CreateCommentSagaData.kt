package org.jesperancinha.newscast.orchestration.saga

/**
 * Created by jofisaes on 06/10/2021
 */
data class CreateCommentSagaData(
    val idPage: Long,
    val pageComment: String,
    val idAuthor: Long,
    val authorComment: String,
    val idMessage: Long,
    val messageComment: String,
)