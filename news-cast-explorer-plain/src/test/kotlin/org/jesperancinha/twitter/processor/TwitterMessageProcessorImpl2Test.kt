package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonParseException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.jesperancinha.twitter.model.db.Author
import org.jesperancinha.twitter.model.db.Message
import org.jesperancinha.twitter.model.db.Page
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.IOException
import java.nio.charset.Charset

@SpringBootTest
internal class TwitterMessageProcessorImpl2Test(
    @Autowired
    val twitterMessageProcessor: TwitterMessageProcessor,
) {

    @MockkBean(relaxed = true)
    lateinit var twitterClient: TwitterClient

    @MockkBean
    lateinit var authorRepository: AuthorRepository

    @MockkBean
    lateinit var messageRepository: MessageRepository

    @MockkBean
    lateinit var pageRepository: PageRepository

    @BeforeEach
    fun setUp() {
        val testPage = Page.builder()
            .id(1L)
            .duration(2000L)
            .createdAt(1550265180555L)
            .build()
        val testAuthor = Author.builder()
            .id(2L)
            .twitterAuthorId("998877665544332211")
            .createdAt(1550265180556L)
            .nMessages(1L)
            .screenName("Author1ScreenName")
            .page(testPage)
            .name("Author1")
            .build()
        val testMessage = Message.builder()
            .id(3L)
//            .author(testAuthor)
            .twitterMessageId("1122333445566778899")
            .text("Message1")
            .createdAt(1550265180557L)
            .build()
        every { pageRepository.save(any<Page>()) } returns testPage
        every { authorRepository.save(any<Author>()) } returns testAuthor
        every { messageRepository.save(any<Message>()) } returns testMessage
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun testProcessAllMessages_whenGoodMessage_OkParse() {
        val resultExample1 = getMessageResource("/example1.json")
        val allMessages = setOf(resultExample1)
        val pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L)
        Assertions.assertThat(pageDto).isNotNull
        Assertions.assertThat(pageDto.duration).isEqualTo(2000L)
        Assertions.assertThat(pageDto.authors).hasSize(1)
        val authorDto = pageDto.authors[0]
        Assertions.assertThat(authorDto).isNotNull
        Assertions.assertThat(authorDto.id).isEqualTo("206")
        Assert.assertEquals("Fail Not Equal!", authorDto.id, "206")
        assertEquals("206", authorDto.id, "Fail Not Equal!")
        Assertions.assertThat(authorDto.name).isEqualTo("lola_montes")
        Assertions.assertThat(authorDto.screenName).isEqualTo("bacalhau_oil")
        Assertions.assertThat(authorDto.createdAt).isEqualTo(1632872907000L)
        Assertions.assertThat(authorDto.messageDtos).isNotNull
        Assertions.assertThat(authorDto.messageDtos).hasSize(1)
        val messageDto = authorDto.messageDtos[0]
        Assertions.assertThat(messageDto).isNotNull
        messageDto.id shouldBe "195"
        Assertions.assertThat(messageDto.text)
            .isEqualTo("tuna california reaper mint beef sugar cod fish salt naga jolokia tuna parsley")
        Assertions.assertThat(messageDto.createdAt).isEqualTo(1632872907000L)
        val capturedPages = mutableListOf<Page>()
        verify(exactly = 2) {
            pageRepository.save(
                capture(capturedPages))
        }
        capturedPages.shouldNotBeNull()
        val page = capturedPages[0]
        page.shouldNotBeNull()
        page.id.shouldBeNull()
        page.authors.shouldBeEmpty()
        val page2 = capturedPages[1]
        page2.shouldNotBeNull()
        page2.id.shouldBe(1)
        page2.authors.size shouldBe 1
        val captureAuthors = mutableListOf<Author>()
        verify(exactly = 2) { authorRepository.save(capture(captureAuthors)) }
        captureAuthors.shouldNotBeNull()
        val author = captureAuthors[0]
        author.shouldNotBeNull()
        author.id.shouldBeNull()
        val author2 = captureAuthors[1]
        author2.shouldNotBeNull()
        Assertions.assertThat(author2.id).isEqualTo(2L)
        val captureMessages = mutableListOf<Message>()
        verify {
            messageRepository.save(
                capture(captureMessages))
        }
        Assertions.assertThat(captureMessages).isNotNull
        captureMessages.size shouldBe 1
        captureMessages[0].id.shouldBeNull()
    }

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")

        val exception = shouldThrow<JsonParseException> {
            twitterMessageProcessor
                .processAllMessages(
                    allMessages,
                    1122333445566778899L,
                    998877665544332211L)

        }
        exception.message.shouldNotBeNull()
    }

    private fun getMessageResource(messageResource: String): String {
        val resourceAsStream1 = javaClass.getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }

}