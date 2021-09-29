package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonProcessingException
import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import org.apache.commons.io.IOUtils
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.jesperancinha.twitter.client.TwitterClient
import org.jesperancinha.twitter.data.AuthorDto
import org.jesperancinha.twitter.data.MessageDto
import org.jesperancinha.twitter.model.db.Author
import org.jesperancinha.twitter.model.db.Message
import org.jesperancinha.twitter.model.db.Page
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Captor
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.nio.charset.Charset

@SpringBootTest
class TwitterMessageProcessorImplJUnit4HamcrestTest(
    @Autowired
    val twitterMessageProcessor: TwitterMessageProcessorImpl,
) {
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


    @BeforeEach
    fun setUp() {
        every { pageRepository.save(ArgumentMatchers.any(Page::class.java)) } returns testPage
        every { authorRepository.save(ArgumentMatchers.any(Author::class.java)) } returns testAuthor
        every { messageRepository.save(ArgumentMatchers.any(Message::class.java)) } returns testMessage
    }

    @Test
    fun testProcessAllMessages_whenGoodMessage_OkParse() {
        val resultExample1 = getMessageResource("/example1.json")
        val allMessages = setOf(resultExample1)
        val pageDto = twitterMessageProcessor.processAllMessages(
            allMessages, 1579079712000L, 1579079714000L)
        MatcherAssert.assertThat(pageDto, Matchers.notNullValue())
        MatcherAssert.assertThat(pageDto.duration, Matchers.`is`(2000L))
        MatcherAssert.assertThat<List<AuthorDto>>(pageDto.authors, Matchers.hasSize(1))
        val authorDto: AuthorDto = pageDto.authors[0]
        MatcherAssert.assertThat(authorDto, Matchers.notNullValue())
        MatcherAssert.assertThat(authorDto.id, Matchers.`is`("999999999000000000"))
        MatcherAssert.assertThat(authorDto.name, Matchers.`is`("Author1"))
        MatcherAssert.assertThat(authorDto.screenName, Matchers.`is`("Author1ScreenName"))
        MatcherAssert.assertThat(authorDto.createdAt, Matchers.`is`(1550265180000L))
        MatcherAssert.assertThat<List<MessageDto>>(authorDto.messageDtos, Matchers.notNullValue())
        MatcherAssert.assertThat<List<MessageDto>>(authorDto.messageDtos, Matchers.hasSize<MessageDto>(1))
        val messageDto: MessageDto = authorDto.messageDtos.get(0)
        MatcherAssert.assertThat(messageDto, Matchers.notNullValue())
        MatcherAssert.assertThat(messageDto.id, Matchers.`is`("999999999000000000"))
        MatcherAssert.assertThat(messageDto.text, Matchers.`is`("Message1"))
        MatcherAssert.assertThat(messageDto.createdAt, Matchers.`is`(1578935617000L))
        Mockito.verify<TwitterClient>(twitterClient, Mockito.atMostOnce()).startFetchProcess()
        Mockito.verify<PageRepository>(pageRepository, Mockito.times(2)).save(pageArgumentCaptor?.capture())
        val pages = pageArgumentCaptor?.allValues
        pages.shouldNotBeNull()
        val page = pages[0]
        MatcherAssert.assertThat(page, Matchers.notNullValue())
        MatcherAssert.assertThat(page.id, Matchers.nullValue())
        MatcherAssert.assertThat<List<Author>>(page.authors,
            Matchers.`is`<Collection<Author>>(Matchers.emptyCollectionOf<Author>(
                Author::class.java)))
        val page2 = pages[1]
        MatcherAssert.assertThat(page2, Matchers.notNullValue())
        MatcherAssert.assertThat(page2.id, Matchers.`is`(1L))
        MatcherAssert.assertThat<List<Author>>(page2.authors, Matchers.hasSize<Author>(1))
        Mockito.verify<AuthorRepository>(authorRepository, Mockito.times(2))
            .save(authorArgumentCaptor?.capture())
        val authors = authorArgumentCaptor?.allValues
        authors.shouldNotBeNull()
        val author: Author = authors[0]
        MatcherAssert.assertThat(author, Matchers.notNullValue())
        MatcherAssert.assertThat(author.id, Matchers.nullValue())
        MatcherAssert.assertThat<List<Message>>(author.messages, Matchers.`is`(Matchers.emptyCollectionOf(
            Message::class.java)))
        val author2: Author = authors[1]
        MatcherAssert.assertThat(author2, Matchers.notNullValue())
        MatcherAssert.assertThat(author2.id, Matchers.`is`(2L))
        MatcherAssert.assertThat<List<Message>>(author2.messages, Matchers.hasSize<Message>(1))
        Mockito.verify<MessageRepository>(messageRepository, Mockito.only())
            .save(messageArgumentCaptor?.capture())
        val message = messageArgumentCaptor?.value
        MatcherAssert.assertThat(message, Matchers.notNullValue())
        MatcherAssert.assertThat(message?.id, Matchers.nullValue())
    }

    private fun getMessageResource(messageResource: String): String {
        val resourceAsStream1 = javaClass.getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }

    @Test
    fun testMessages_whenMessageListInvalid_throwException() {
        val allMessages = setOf("this is not a JSON", "And this is also not one!")

        val exception = shouldThrow<JsonProcessingException> {
            twitterMessageProcessor.processAllMessages(allMessages, 1122333445566778899L, 998877665544332211L)
        }
        exception.message.shouldNotBeNull()

    }

    @AfterEach
    fun tearDown() {
        Mockito.mockitoSession().initMocks(twitterClient)
    }

    companion object {
        private var testPage: Page? = null
        private var testAuthor: Author? = null
        private var testMessage: Message? = null

        @BeforeAll
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