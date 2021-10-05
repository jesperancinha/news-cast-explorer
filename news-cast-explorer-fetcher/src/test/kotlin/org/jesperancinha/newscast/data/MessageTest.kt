package org.jesperancinha.newscast.data

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class MessageTest {
    @Test
    fun testEquals_whenEquals_Ok() {
        val messageDto1 = MessageDto.builder().id("dsfsfndsivnka324")
            .createdAt(111L)
            .text("wowowow")
            .build()
        val messageDto2 = MessageDto.builder().id("dsfsfndsivnka324")
            .createdAt(1234324L)
            .text("nononono")
            .build()
        Assertions.assertThat(messageDto1).isEqualTo(messageDto2)
    }

    @Test
    fun testHashCode_whenOther_NotSame() {
        val messageDto1 = MessageDto.builder().id("dsfsfndsivnka324")
            .createdAt(111L)
            .text("wowowow")
            .build()
        val messageDto2 = MessageDto.builder().id("dsfsfndsivnka324")
            .createdAt(1234324L)
            .text("nononono")
            .build()
        Assertions.assertThat(messageDto1).isNotSameAs(messageDto2)
        Assertions.assertThat(messageDto1.hashCode()).isEqualTo(messageDto2.hashCode())
    }
}