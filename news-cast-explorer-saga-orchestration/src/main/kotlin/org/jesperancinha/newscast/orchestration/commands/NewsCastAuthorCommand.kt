package org.jesperancinha.newscast.orchestration.commands

/**
 * Created by jofisaes on 06/10/2021
 */
data class NewsCastAuthorCommand(
    val idAuthor: Long,
    val authorComment: String,
)