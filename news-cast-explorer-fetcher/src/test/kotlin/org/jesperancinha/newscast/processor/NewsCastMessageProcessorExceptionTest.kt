package org.jesperancinha.newscast.processor

import com.fasterxml.jackson.core.JsonProcessingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.mockk
import org.junit.jupiter.api.Test

class NewsCastMessageProcessorExceptionTest {
    private val newsCastMessageProcessor: NewsCastMessageProcessor = NewsCastMessageProcessor
        .builder()
        .messageRepository(mockk())
        .authorRepository(mockk())
        .pageRepository(mockk())
        .build()

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")
        val exception = shouldThrow<JsonProcessingException> {
            newsCastMessageProcessor
                .processAllMessages(allMessages, 1122333445566778899L, 998877665544332211L)
        }
        exception.message.shouldNotBeNull()

    }
}