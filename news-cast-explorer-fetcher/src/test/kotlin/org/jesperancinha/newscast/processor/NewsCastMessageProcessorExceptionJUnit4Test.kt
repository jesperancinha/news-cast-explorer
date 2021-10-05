package org.jesperancinha.newscast.processor

import com.fasterxml.jackson.core.JsonProcessingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import org.jesperancinha.newscast.repository.AuthorRepository
import org.jesperancinha.newscast.repository.MessageRepository
import org.jesperancinha.newscast.repository.PageRepository
import org.junit.Test
import org.mockito.Mockito

class NewsCastMessageProcessorExceptionJUnit4Test {
    private val newsCastMessageProcessor: NewsCastMessageProcessor = NewsCastMessageProcessor
        .builder()
        .messageRepository(Mockito.mock(MessageRepository::class.java))
        .authorRepository(Mockito.mock(AuthorRepository::class.java))
        .pageRepository(Mockito.mock(PageRepository::class.java))
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