package org.jesperancinha.newscast.orchestration.controller

import org.jesperancinha.newscast.orchestration.saga.CreateCommentSagaData
import org.jesperancinha.newscast.orchestration.service.NewsCastTicketService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by jofisaes on 06/10/2021
 */
@RestController
@RequestMapping("/api/saga/orchestration")
class NewsCastCommentController(
    val newsCastTicketService: NewsCastTicketService,
) {

    @PostMapping
    fun postComments(createCommentSagaData: CreateCommentSagaData) {
        newsCastTicketService.createOrder(createCommentSagaData)
    }
}