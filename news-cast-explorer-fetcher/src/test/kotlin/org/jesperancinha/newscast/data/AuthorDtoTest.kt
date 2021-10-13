package org.jesperancinha.newscast.data

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.junit.jupiter.api.Test

class AuthorDtoTest {
    @Test
    fun testEquals_whenEquals_Ok() {
        val authorDto1 = AuthorDto.builder().newsCastId("98whef9w8efh8f8e9wh")
            .createdAt(111L)
            .name("James")
            .userName("Tim Booth")
            .messages(ArrayList())
            .build()
        val authorDto2 = AuthorDto.builder().newsCastId("98whef9w8efh8f8e9wh")
            .createdAt(1234324L)
            .name("The Smiths")
            .userName("Morrissey")
            .messages(ArrayList())
            .build()
        authorDto1 shouldBe authorDto2
    }

    @Test
    fun testHashCode_whenOther_NotSame() {
        val authorDto1 = AuthorDto.builder().newsCastId("98whef9w8efh8f8e9wh")
            .createdAt(111L)
            .name("James")
            .userName("Tim Booth")
            .messages(ArrayList())
            .build()
        val authorDto2 = AuthorDto.builder().newsCastId("98whef9w8efh8f8e9wh")
            .createdAt(1234324L)
            .name("The Smiths")
            .userName("Jim Morrissey")
            .messages(ArrayList())
            .build()
        authorDto1.shouldNotBeSameInstanceAs(authorDto2)
        authorDto1.hashCode() shouldBe authorDto2.hashCode()
    }
}