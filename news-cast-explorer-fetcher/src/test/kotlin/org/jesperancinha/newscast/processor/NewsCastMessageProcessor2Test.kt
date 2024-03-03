package org.jesperancinha.newscast.processor

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
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
import org.springframework.test.annotation.DirtiesContext
import java.nio.charset.Charset

@SpringBootTest(
    properties = ["org.jesperancinha.newscast.host=http://localhost:8080"]
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class NewsCastMessageProcessor2Test(
    @Autowired
    val newsCastMessageProcessor: NewsCastMessageProcessor,
) : AbstractNCTest() {

    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @MockkBean
    lateinit var authorRepository: AuthorRepository

    @MockkBean
    lateinit var messageRepository: MessageRepository

    @MockkBean
    lateinit var pageRepository: PageRepository

    private val testPage: Page = Page.builder()
        .id(1L)
        .duration(2000L)
        .createdAt(1550265180555L)
        .build()
    private val testAuthor: Author = Author.builder()
        .id(2L)
        .newsCastAuthorId("998877665544332211")
        .createdAt(1550265180556L)
        .nMessages(1)
        .userName("Author1ScreenName")
        .page(testPage)
        .name("Author1")
        .build()
    val testMessage: Message = Message.builder()
        .id(3L)
        .author(testAuthor)
        .newscastMessageId("1122333445566778899")
        .text("Message1")
        .createdAt(1550265180557L)
        .build()

    @BeforeEach
    fun setUp() {
        every { pageRepository.save(any<Page>()) } returns testPage
        every { authorRepository.save(any<Author>()) } returns testAuthor
        every { messageRepository.save(any<Message>()) } returns testMessage
    }

    @Test
    fun testProcessAllMessages_whenGoodMessage_OkParse() {
        val resultExample1 = getMessageResource("/example1.json")
        val allMessages = setOf(resultExample1)
        every { authorRepository.findFirstByNewsCastAuthorIdAndPageId(any(), any()) } returns null
        every { messageRepository.findFirstByNewscastMessageIdAndAuthorId(any(), any()) } returns null
        val pageDto = newsCastMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L)
        pageDto.shouldNotBeNull()
        pageDto.duration shouldBe 2000L
        pageDto.authors.shouldHaveSize(1)
        val authorDto = pageDto.authors[0]
        authorDto.shouldNotBeNull()
        authorDto.newsCastId shouldBe "4"
        authorDto.id.shouldBeNull()
        authorDto.name shouldBe "woman_super"
        authorDto.userName shouldBe "bacalhau_oil"
        authorDto.createdAt shouldBe 1632872907000L
        authorDto.messages.shouldNotBeNull()
        authorDto.messages.shouldHaveSize(1)
        val messageDto = authorDto.messages[0]
        messageDto.shouldNotBeNull()
        messageDto.newsCastId shouldBe "195"
        messageDto.text shouldBe "tuna california reaper mint beef sugar cod fish salt naga jolokia tuna parsley"
        messageDto.createdAt shouldBe 1632872907000L
        val capturedPages = mutableListOf<Page>()
        verify(exactly = 2) {
            pageRepository.save(
                capture(capturedPages)
            )
        }
        capturedPages.shouldNotBeNull()
        capturedPages.shouldHaveSize(2)
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
        captureAuthors.shouldHaveSize(2)
        captureAuthors.shouldNotBeNull()
        val author = captureAuthors[0]
        author.shouldNotBeNull()
        author.id.shouldBeNull()
        val author2 = captureAuthors[1]
        author2.shouldNotBeNull()
        author2.id shouldBe 2L
        val captureMessages = mutableListOf<Message>()
        verify {
            messageRepository.save(
                capture(captureMessages)
            )
        }
        captureMessages.shouldNotBeNull()
        captureMessages.size shouldBe 1
        captureMessages[0].id.shouldBeNull()
    }

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")

        val exception = shouldThrowAny {
            newsCastMessageProcessor
                .processAllMessages(
                    allMessages,
                    1122333445566778899L,
                    998877665544332211L
                )

        }
        exception.message.shouldNotBeNull()
    }

    private fun getMessageResource(messageResource: String): String {
        val resourceAsStream1 = javaClass.getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }

}