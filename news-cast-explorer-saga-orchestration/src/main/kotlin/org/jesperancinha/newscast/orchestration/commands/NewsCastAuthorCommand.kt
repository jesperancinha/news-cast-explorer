package org.jesperancinha.newscast.orchestration.commands

import io.eventuate.tram.commands.common.Command

/**
 * Created by jofisaes on 06/10/2021
 */
data class NewsCastAuthorCommand(
    val idAuthor: Long?,
    val requestId: Long?,
    val comment: String?,
) : Command {
    constructor() : this(null, null, null)
}