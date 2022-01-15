package org.jesperancinha.newscast.processor

import com.fasterxml.jackson.core.JsonProcessingException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.verify
import org.apache.commons.io.IOUtils
import org.jesperancinha.newscast.client.NewsCastClient
import org.jesperancinha.newscast.model.explorer.Author
import org.jesperancinha.newscast.model.explorer.Message
import org.jesperancinha.newscast.model.explorer.Page
import org.jesperancinha.newscast.repository.AuthorRepository
import org.jesperancinha.newscast.repository.MessageRepository
import org.jesperancinha.newscast.repository.PageRepository
import org.jesperancinha.newscast.utils.AbstractNCTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.charset.Charset

@SpringBootTest(properties = ["org.jesperancinha.newscast.host=127.0.0.1"])
class NewsCastMessageProcessor1Test(
    @Autowired
    private val messageProcessor: NewsCastMessageProcessor
) : AbstractNCTest() {
    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @MockkBean(relaxed = true)
    lateinit var authorRepository: AuthorRepository

    @MockkBean(relaxed = true)
    lateinit var messageRepository: MessageRepository

    @MockkBean(relaxed = true)
    lateinit var pageRepository: PageRepository

    private val messageArgumentCaptor = mutableListOf<Message>()

    private val authorArgumentCaptor = mutableListOf<Author>()

    private val pageArgumentCaptor = mutableListOf<Page>()

    @BeforeEach
    fun setUp() {
        every { pageRepository.save(any()) } returns testPage
        every { authorRepository.save(any()) } returns testAuthor
        every { authorRepository.findFirstByNewsCastAuthorIdAndPageId(any(), any()) } returns null
        every { messageRepository.save(any()) } returns testMessage
        every { messageRepository.findFirstByNewscastMessageIdAndAuthorId(any(), any()) } returns null
    }

    @Test
    fun testProcessAllMessages_whenGoodMessage_OkParse() {
        val resultExample1 = getMessageResource("/example1.json")
        val allMessages = setOf(resultExample1)
        val pageDto = messageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L)
        pageDto.shouldNotBeNull()
        pageDto.duration shouldBe 2000L
        pageDto.authors.shouldHaveSize(1)
        val authorDto = pageDto.authors[0]
        authorDto.shouldNotBeNull()
        authorDto.id.shouldBeNull()
        authorDto.name shouldBe "woman_super"
        authorDto.userName shouldBe "bacalhau_oil"
        authorDto.createdAt shouldBe 1632872907000
        authorDto.messages.shouldNotBeNull()
        authorDto.messages.shouldHaveSize(1)
        val messageDto = authorDto.messages[0]
        messageDto.shouldNotBeNull()
        messageDto.newsCastId shouldBe "195"
        messageDto.text shouldBe "tuna california reaper mint beef sugar cod fish salt naga jolokia tuna parsley"
        messageDto.createdAt shouldBe 1632872907000L
        verify(exactly = 2) {
            pageRepository.save(
                capture(pageArgumentCaptor)
            )
        }
        val pages = pageArgumentCaptor
        pages.shouldNotBeNull()
        pages.shouldHaveSize(2)
        val page = pages[0]
        page.shouldNotBeNull()
        page.id.shouldBeNull()
        page.authors.shouldBeEmpty()
        val page2 = pages[1]
        page2.shouldNotBeNull()
        page2.id shouldBe 1L
        page2.authors.shouldHaveSize(1)
        verify(exactly = 2) { authorRepository.save(capture(authorArgumentCaptor)) }
        val authors = authorArgumentCaptor
        authors.shouldNotBeNull()
        authors.shouldHaveSize(2)
        val author = authors[0]
        author.shouldNotBeNull()
        author.id.shouldBeNull()
        val author2 = authors[1]
        author2.shouldNotBeNull()
        author2.id shouldBe 2L
        verify(exactly = 1) { messageRepository.save(capture(messageArgumentCaptor)) }
        val message = messageArgumentCaptor[0]
        message.shouldNotBeNull()
        message.id.shouldBeNull()
    }

    private fun getMessageResource(messageResource: String): String {
        val resourceAsStream1 = javaClass.getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")

        val exception = shouldThrow<JsonProcessingException> {
            messageProcessor
                .processAllMessages(
                    allMessages,
                    1122333445566778899L,
                    998877665544332211L
                )
        }
        exception.message.shouldContain("this is not a JSON")
    }

    companion object {
        private var testPage: Page = Page.builder()
            .id(1L)
            .duration(2000L)
            .createdAt(1550265180555L)
            .build()
        private var testAuthor: Author = Author.builder()
            .id(2L)
            .newsCastAuthorId("998877665544332211")
            .createdAt(1550265180556L)
            .nMessages(1)
            .userName("Author1ScreenName")
            .page(testPage)
            .name("Author1")
            .build()
        private var testMessage: Message = Message.builder()
            .id(3L)
            .newscastMessageId("1122333445566778899")
            .text("Message1")
            .createdAt(1550265180557L)
            .build()
    }
}