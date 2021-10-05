package org.jesperancinha.newscast.rest

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.StandardCharsets


/**
 * Created by jofisaes on 04/10/2021
 */
@RestController
@RequestMapping("/")
class TwitterController {

    @GetMapping("/1.1/statuses/filter.json")
    fun filterJsonGet(
        @RequestParam("delimited") delimited: String, @RequestParam("stall_warnings") stallWarnings: Boolean,
    ): List<ObjectNode> {
        return FILES
    }

    @PostMapping("/1.1/statuses/filter.json")
    fun filterJsonPost(
        @RequestParam("delimited") delimited: String, @RequestParam("stall_warnings") stallWarnings: Boolean,
    ): List<ObjectNode> {
        return FILES
    }

    companion object {
        val mapper = ObjectMapper()
        val factory: JsonFactory = mapper.jsonFactory
        val FILES: List<ObjectNode> = (1..15).map {
            (javaClass.getResourceAsStream("/example${it}.json")
                ?.let { stream -> String(stream.readAllBytes(), StandardCharsets.UTF_8) } ?: "")
                .let { envelope -> mapper.readTree(factory.createParser(envelope)) }
        }
    }
}