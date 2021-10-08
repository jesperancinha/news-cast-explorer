package org.jesperancinha.newscast.cdc

/**
 * Created by jofisaes on 06/10/2021
 */
data class NewsCastAuthorCommand(
    val idAuthor: Long?,
    val requestId: Long?,
    val comment: String?,
) {
    constructor() : this(null, null, null)
}