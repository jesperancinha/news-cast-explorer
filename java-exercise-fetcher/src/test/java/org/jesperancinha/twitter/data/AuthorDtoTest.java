package org.jesperancinha.twitter.data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorDtoTest {

    @Test
    public void testEquals_whenEquals_Ok() {
        final AuthorDto authorDto1 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
                .createdAt(111L)
                .name("James")
                .screenName("Tim Booth")
                .messageDtos(new ArrayList<>())
                .build();

        final AuthorDto authorDto2 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
                .createdAt(1234324L)
                .name("The Smiths")
                .screenName("Jim Morrissey")
                .messageDtos(new ArrayList<>())
                .build();

        assertThat(authorDto1).isEqualTo(authorDto2);
    }

    @Test
    public void testHashCode_whenOther_NotSame() {
        final AuthorDto authorDto1 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
                .createdAt(111L)
                .name("James")
                .screenName("Tim Booth")
                .messageDtos(new ArrayList<>())
                .build();

        final AuthorDto authorDto2 = AuthorDto.builder().id("98whef9w8efh8f8e9wh")
                .createdAt(1234324L)
                .name("The Smiths")
                .screenName("Jim Morrissey")
                .messageDtos(new ArrayList<>())
                .build();


        assertThat(authorDto1).isNotSameAs(authorDto2);
        assertThat(authorDto1.hashCode()).isEqualTo(authorDto2.hashCode());

    }
}