package org.jesperancinha.newscast.saga.data

/**
 * Created by jofisaes on 06/10/2021
 */
data class NewsCastComments(
    val idPage: Long? = null,
    val pageComment: String? = null,
    val idAuthor: Long? = null,
    val authorComment: String? = null,
    val idMessage: Long? = null,
    val messageComment: String? = null,
    var authorRequestId: Long? = null,
    var pageRequestId:Long? = null,
    var messageRequestId:Long? = null
)