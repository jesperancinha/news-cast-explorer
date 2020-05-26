package org.jesperancinha.twitter.data;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    @Test
    public void testEquals_whenEquals_Ok() {
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
    public void testHashCode_whenOther_NotSame() {
        final MessageDto messageDto1 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(111L)
                .text("wowowow")
                .build();

        final MessageDto messageDto2 = MessageDto.builder().id("dsfsfndsivnka324")
                .createdAt(1234324L)
                .text("nononono")
                .build();

        assertThat(messageDto1).isNotSameAs(messageDto2);
        assertThat(messageDto1.hashCode()).isEqualTo(messageDto2.hashCode());

    }
}