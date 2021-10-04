package org.jesperancinha.twitter.rest

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
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
        val mapper = ObjectMapper()
        val factory: JsonFactory = mapper.getJsonFactory()
        return FILES.map {
            val parser: JsonParser = factory.createJsonParser(it)
            mapper.readTree(parser)
        }
    }

    @PostMapping("/1.1/statuses/filter.json")
    fun filterJsonPost(
        @RequestParam("delimited") delimited: String, @RequestParam("stall_warnings") stallWarnings: Boolean,
    ): List<ObjectNode> {
        val mapper = ObjectMapper()
        val factory: JsonFactory = mapper.getJsonFactory()


        return FILES.map {
            val parser: JsonParser = factory.createJsonParser(it)
            mapper.readTree(parser)
        }
    }

    companion object {
        val FILES = (1..10).map {
            javaClass.getResourceAsStream("/example${it}.json")
                ?.let { stream -> String(stream.readAllBytes(), StandardCharsets.UTF_8) } ?: ""
        }
    }
}