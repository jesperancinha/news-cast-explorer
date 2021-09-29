package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonProcessingException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
import org.junit.Test
import org.mockito.Mockito

class TwitterMessageProcessorImplJUnit4HamcrestExceptionTest {
    private val twitterMessageProcessor: TwitterMessageProcessor = TwitterMessageProcessorImpl
        .builder()
        .messageRepository(Mockito.mock(MessageRepository::class.java))
        .authorRepository(Mockito.mock(AuthorRepository::class.java))
        .pageRepository(Mockito.mock(PageRepository::class.java))
        .build()

    @Test
    @Throws(JsonProcessingException::class)
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")
        val exception = shouldThrow<JsonProcessingException> {
            twitterMessageProcessor
                .processAllMessages(allMessages, 1122333445566778899L, 998877665544332211L)
        }
        exception.message.shouldContain("Expected BEGIN_OBJECT but was STRING at line 1 column 1 path")

    }
}