package org.jesperancinha.newscast.data

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test

class MessageTest {
    @Test
    fun testEquals_whenEquals_Ok() {
        val messageDto1 = MessageDto.builder().newsCastId("dsfsfndsivnka324")
            .createdAt(111L)
            .text("wowowow")
            .build()
        val messageDto2 = MessageDto.builder().newsCastId("dsfsfndsivnka324")
            .createdAt(1234324L)
            .text("nononono")
            .build()
        messageDto1 shouldBe messageDto2
    }

    @Test
    fun testHashCode_whenOther_NotSame() {
        val messageDto1 = MessageDto.builder().newsCastId("dsfsfndsivnka324")
            .createdAt(111L)
            .text("wowowow")
            .build()
        val messageDto2 = MessageDto.builder().newsCastId("dsfsfndsivnka324")
            .createdAt(1234324L)
            .text("nononono")
            .build()
        messageDto1 shouldNotBeSameInstanceAs messageDto2
        messageDto1.hashCode() shouldBe messageDto2.hashCode()
    }
}