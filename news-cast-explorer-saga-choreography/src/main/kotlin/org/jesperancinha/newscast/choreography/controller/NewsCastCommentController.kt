package org.jesperancinha.newscast.choreography.controller

import org.jesperancinha.newscast.choreography.service.NewsCastTicketService
import org.jesperancinha.newscast.saga.data.NewsCastComments
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
@RequestMapping("choreography")
class NewsCastCommentController(
    @Autowired
    val newsCastTicketService: NewsCastTicketService,
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postComments(@RequestBody commentSagaData: NewsCastComments) {
        newsCastTicketService.createNewsCastComments(commentSagaData)
    }

    @GetMapping
    fun test(): String = "OK"
}