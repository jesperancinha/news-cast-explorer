package org.jesperancinha.newscast.orchestration.controller

import org.jesperancinha.newscast.orchestration.saga.CreateCommentSagaData
import org.jesperancinha.newscast.orchestration.service.NewsCastTicketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by jofisaes on 06/10/2021
 */
@RestController
@RequestMapping("orchestration")
class NewsCastCommentController(
    @Autowired
    val newsCastTicketService: NewsCastTicketService,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postComments(@RequestBody createCommentSagaData: CreateCommentSagaData) {
        newsCastTicketService.createOrder(createCommentSagaData)
    }

    @GetMapping
    fun test(): String = "OK"
}