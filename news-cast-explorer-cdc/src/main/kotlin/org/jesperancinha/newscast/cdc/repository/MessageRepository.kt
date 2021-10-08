package org.jesperancinha.newscast.cdc.repository

import org.jesperancinha.newscast.cdc.domain.Message
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by jofisaes on 08/10/2021
 */
interface MessageRepository :JpaRepository<Message, Long>