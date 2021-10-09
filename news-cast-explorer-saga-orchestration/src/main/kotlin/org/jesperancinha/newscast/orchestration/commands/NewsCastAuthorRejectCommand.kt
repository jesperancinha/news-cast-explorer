package org.jesperancinha.newscast.orchestration.commands

import io.eventuate.tram.commands.common.Command

/**
 * Created by jofisaes on 06/10/2021
 */
data class NewsCastAuthorRejectCommand(
    val requestId: Long?,
) : Command {
    constructor() : this(null)
}