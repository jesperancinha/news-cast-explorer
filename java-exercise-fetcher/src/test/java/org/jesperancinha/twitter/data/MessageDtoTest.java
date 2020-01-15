package org.jesperancinha.twitter.data;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDtoTest {

    @Test
    public void testEquals() {
        final MessageDto messageDto1 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(111L)
                .text("wowowow")
                .build();

        final MessageDto messageDto2 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(1234324L)
                .text("nononono")
                .build();

        assertThat(messageDto1).isEqualTo(messageDto2);
    }

    @Test
    public void testHashCode() {
        final MessageDto messageDto1 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(111L)
                .text("wowowow")
                .build();

        final MessageDto messageDto2 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(1234324L)
                .text("nononono")
                .build();

        assertThat(messageDto1).isNotSameAs(messageDto2);
    }
}