package org.jesperancinha.twitter.data

import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AuthorDtoTest {
    @Test
    fun testEquals_whenEquals_Ok() {
        val authorDto1 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
            .createdAt(111L)
            .name("James")
            .screenName("Tim Booth")
            .messageDtos(ArrayList())
            .build()
        val authorDto2 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
            .createdAt(1234324L)
            .name("The Smiths")
            .screenName("Morrissey")
            .messageDtos(ArrayList())
            .build()
        Assertions.assertThat(authorDto1).isEqualTo(authorDto2)
    }

    @Test
    fun testHashCode_whenOther_NotSame() {
        val authorDto1 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
            .createdAt(111L)
            .name("James")
            .screenName("Tim Booth")
            .messageDtos(ArrayList())
            .build()
        val authorDto2 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
            .createdAt(1234324L)
            .name("The Smiths")
            .screenName("Jim Morrissey")
            .messageDtos(ArrayList())
            .build()
        authorDto1.shouldNotBeSameInstanceAs(authorDto2)
        Assertions.assertThat(authorDto1.hashCode()).isEqualTo(authorDto2.hashCode())
    }
}