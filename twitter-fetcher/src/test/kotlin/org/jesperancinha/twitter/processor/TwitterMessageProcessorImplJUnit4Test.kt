package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonProcessingException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import org.apache.commons.io.IOUtils
import org.assertj.core.api.Assertions
import org.jesperancinha.twitter.client.TwitterClient
import org.jesperancinha.twitter.model.db.Author
import org.jesperancinha.twitter.model.db.Message
import org.jesperancinha.twitter.model.db.Page
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
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
class TwitterMessageProcessorImplJUnit4Test {
    @Autowired
    private val twitterMessageProcessor: TwitterMessageProcessor? = null

    @MockkBean(relaxed = true)
    lateinit var twitterClient: TwitterClient

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
        val pageDto = twitterMessageProcessor!!.processAllMessages(allMessages, 1579079712000L, 1579079714000L)
        Assertions.assertThat(pageDto).isNotNull
        Assertions.assertThat(pageDto.duration).isEqualTo(2000L)
        Assertions.assertThat(pageDto.authors).hasSize(1)
        val authorDto = pageDto.authors[0]
        Assertions.assertThat(authorDto).isNotNull
        Assertions.assertThat(authorDto.id).isEqualTo("999999999000000000")
        Assertions.assertThat(authorDto.name).isEqualTo("Author1")
        Assertions.assertThat(authorDto.screenName).isEqualTo("Author1ScreenName")
        Assertions.assertThat(authorDto.createdAt).isEqualTo(1550265180000L)
        Assertions.assertThat(authorDto.messageDtos).isNotNull
        Assertions.assertThat(authorDto.messageDtos).hasSize(1)
        val messageDto = authorDto.messageDtos[0]
        Assertions.assertThat(messageDto).isNotNull
        Assertions.assertThat(messageDto.id).isEqualTo("999999999000000000")
        Assertions.assertThat(messageDto.text).isEqualTo("Message1")
        Assertions.assertThat(messageDto.createdAt).isEqualTo(1578935617000L)
        Mockito.verify(twitterClient, Mockito.atMostOnce())?.startFetchProcess()
        Mockito.verify(pageRepository, Mockito.times(2))?.save(
            pageArgumentCaptor!!.capture())
        val pages = pageArgumentCaptor?.allValues
        pages.shouldNotBeNull()
        val page = pages[0]
        Assertions.assertThat(page).isNotNull
        Assertions.assertThat(page.id).isNull()
        Assertions.assertThat(page.authors).isEmpty()
        val page2 = pages[1]
        Assertions.assertThat(page2).isNotNull
        Assertions.assertThat(page2.id).isEqualTo(1L)
        Assertions.assertThat(page2.authors).hasSize(1)
        Mockito.verify(authorRepository, Mockito.times(2))?.save(authorArgumentCaptor!!.capture())
        val authors = authorArgumentCaptor?.allValues
        authors.shouldNotBeNull()
        val author = authors[0]
        Assertions.assertThat(author).isNotNull
        Assertions.assertThat(author.id).isNull()
        val author2 = authors[1]
        Assertions.assertThat(author2).isNotNull
        Assertions.assertThat(author2.id).isEqualTo(2L)
        Mockito.verify(messageRepository, Mockito.only())?.save(
            messageArgumentCaptor!!.capture())
        val message = messageArgumentCaptor?.value
        Assertions.assertThat(message).isNotNull
        Assertions.assertThat(message?.id).isNull()
    }

    private fun getMessageResource(messageResource: String): String {
        val resourceAsStream1 = javaClass.getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")

        val exception = shouldThrow<JsonProcessingException> {
            twitterMessageProcessor
                ?.processAllMessages(
                    allMessages,
                    1122333445566778899L,
                    998877665544332211L)
        }
        exception.message.shouldContain("Expected BEGIN_OBJECT but was STRING at line 1 column 1 path");
    }

    @After
    fun tearDown() {
        Mockito.mockitoSession().initMocks(twitterClient)
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
                .twitterAuthorId("998877665544332211")
                .createdAt(1550265180556L)
                .nMessages(1L)
                .screenName("Author1ScreenName")
                .page(testPage)
                .name("Author1")
                .build()
            testMessage = Message.builder()
                .id(3L)
                .author(testAuthor)
                .twitterMessageId("1122333445566778899")
                .text("Message1")
                .createdAt(1550265180557L)
                .build()
        }
    }
}