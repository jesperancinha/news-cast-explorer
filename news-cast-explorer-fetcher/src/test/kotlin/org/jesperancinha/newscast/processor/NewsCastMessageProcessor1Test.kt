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
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.IOException
import java.nio.charset.Charset

@SpringBootTest
class NewsCastMessageProcessor1Test {
    @Autowired
    private val messageProcessor: NewsCastMessageProcessor? = null

    @MockkBean(relaxed = true)
    lateinit var newsCastClient: NewsCastClient

    @MockkBean(relaxed = true)
    lateinit var authorRepository: AuthorRepository

    @MockkBean(relaxed = true)
    lateinit var messageRepository: MessageRepository

    @MockkBean(relaxed = true)
    lateinit var pageRepository: PageRepository

    @Captor
    private val messageArgumentCaptor: ArgumentCaptor<Message>? = null

    @Captor
    private val authorArgumentCaptor: ArgumentCaptor<Author>? = null

    @Captor
    private val pageArgumentCaptor: ArgumentCaptor<Page>? = null

    @Before
    fun setUp() {
        every { pageRepository.save(ArgumentMatchers.any(Page::class.java)) } returns testPage
        every { authorRepository.save(ArgumentMatchers.any(Author::class.java)) } returns testAuthor
        every { messageRepository.save(ArgumentMatchers.any(Message::class.java)) } returns testMessage
    }

    @Test
    @Throws(IOException::class, InterruptedException::class)
    fun testProcessAllMessages_whenGoodMessage_OkParse() {
        val resultExample1 = getMessageResource("/example1.json")
        val allMessages = setOf(resultExample1)
        val pageDto = messageProcessor!!.processAllMessages(allMessages, 1579079712000L, 1579079714000L)
        pageDto.shouldNotBeNull()
        pageDto.duration shouldBe 2000L
        pageDto.authors.shouldHaveSize(1)
        val authorDto = pageDto.authors[0]
        authorDto.shouldNotBeNull()
        authorDto.id shouldBe "999999999000000000"
        authorDto.name shouldBe "Author1"
        authorDto.screenName shouldBe "Author1ScreenName"
        authorDto.createdAt shouldBe 1550265180000L
        authorDto.messageDtos.shouldNotBeNull()
        authorDto.messageDtos.shouldHaveSize(1)
        val messageDto = authorDto.messageDtos[0]
        messageDto.shouldNotBeNull()
        messageDto.id shouldBe "999999999000000000"
        messageDto.text shouldBe "Message1"
        messageDto.createdAt shouldBe 1578935617000L
        Mockito.verify(newsCastClient, Mockito.atMostOnce())?.startFetchProcess()
        verify(exactly = 2) {
            pageRepository.save(
                pageArgumentCaptor!!.capture())
        }
        val pages = pageArgumentCaptor?.allValues
        pages.shouldNotBeNull()
        val page = pages[0]
        page.shouldNotBeNull()
        page.id.shouldBeNull()
        page.authors.shouldBeEmpty()
        val page2 = pages[1]
        page2.shouldNotBeNull()
        page2.id shouldBe 1L
        page2.authors.shouldHaveSize(1)
        verify(exactly = 2) { authorRepository.save(authorArgumentCaptor!!.capture()) }
        val authors = authorArgumentCaptor?.allValues
        authors.shouldNotBeNull()
        val author = authors[0]
        author.shouldNotBeNull()
        author.id.shouldBeNull()
        val author2 = authors[1]
        author2.shouldNotBeNull()
        author2.id shouldBe 2L
        verify {
            messageRepository.save(
                messageArgumentCaptor!!.capture())
        }
        val message = messageArgumentCaptor?.value
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
                ?.processAllMessages(
                    allMessages,
                    1122333445566778899L,
                    998877665544332211L)
        }
        exception.message.shouldContain("Expected BEGIN_OBJECT but was STRING at line 1 column 1 path")
    }

    @After
    fun tearDown() {
        Mockito.mockitoSession().initMocks(newsCastClient)
    }

    companion object {
        private var testPage: Page? = null
        private var testAuthor: Author? = null
        private var testMessage: Message? = null

        @BeforeClass
        fun beforeAll() {
            testPage = Page.builder()
                .id(1L)
                .duration(2000L)
                .createdAt(1550265180555L)
                .build()
            testAuthor = Author.builder()
                .id(2L)
                .newsCastAuthorId("998877665544332211")
                .createdAt(1550265180556L)
                .nMessages(1L)
                .screenName("Author1ScreenName")
                .page(testPage)
                .name("Author1")
                .build()
            testMessage = Message.builder()
                .id(3L)
                .newscastMessageId("1122333445566778899")
                .text("Message1")
                .createdAt(1550265180557L)
                .build()
        }
    }
}