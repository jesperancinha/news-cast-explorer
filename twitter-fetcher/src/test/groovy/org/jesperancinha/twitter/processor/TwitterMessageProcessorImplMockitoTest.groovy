package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonProcessingException
import org.apache.commons.io.IOUtils
import org.assertj.core.api.SoftAssertions
import org.jesperancinha.twitter.client.TwitterClient
import org.jesperancinha.twitter.model.db.Author
import org.jesperancinha.twitter.model.db.Message
import org.jesperancinha.twitter.model.db.Page
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.nio.charset.Charset

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.*
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class TwitterMessageProcessorImplMockitoTest extends Specification {

    @Autowired
    private TwitterMessageProcessor twitterMessageProcessor

    @MockBean
    private TwitterClient twitterClient

    @MockBean
    private AuthorRepository authorRepository

    @MockBean
    private MessageRepository messageRepository

    @MockBean
    private PageRepository pageRepository

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor

    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor

    @Captor
    private ArgumentCaptor<Page> pageArgumentCaptor

    private static Page testPage

    private static Author testAuthor

    private static Message testMessage

    void setup() {
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
        when(pageRepository.save(any(Page.class))).thenReturn(testPage)
        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor)
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage)
    }

    void cleanup() {
    }

    def "Should process messages with example 1"() {
        given:
        def resultExample1 = getMessageResource("/example1.json")
        def allMessages = Set.of(resultExample1)

        when:
        def pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L)

        then:
        assertThat(pageDto).isNotNull()
        assertThat(pageDto.getDuration()).isEqualTo(2000L)
        assertThat(pageDto.getAuthors()).hasSize(1)
        def authorDto = pageDto.getAuthors().get(0)
        assertThat(authorDto).isNotNull()
        assertThat(authorDto.getId()).isEqualTo("206")
        assertThat(authorDto.getName()).isEqualTo("lola_montes")
        assertThat(authorDto.getScreenName()).isEqualTo("bacalhau_oil")
        assertThat(authorDto.getCreatedAt()).isEqualTo(1632872907000L)
        assertThat(authorDto.getMessageDtos()).isNotNull()
        assertThat(authorDto.getMessageDtos()).hasSize(1)
        def messageDto = authorDto.getMessageDtos().get(0)
        assertThat(messageDto).isNotNull()
        assertThat(messageDto.getId()).isEqualTo("195")
        assertThat(messageDto.getText()).isEqualTo("tuna california reaper mint beef sugar cod fish salt naga jolokia tuna parsley")
        assertThat(messageDto.getCreatedAt()).isEqualTo(1632872907000L)

        SoftAssertions.assertSoftly { softly ->
            verify(twitterClient, atMostOnce()).startFetchProcess()
            verify(pageRepository, times(2)).save(pageArgumentCaptor.capture())
            def pages = pageArgumentCaptor.getAllValues()
            def page = pages.get(0)
            assertThat(page).isNotNull()
            assertThat(page.getId()).isNull()
            assertThat(page.getAuthors()).isEmpty()
            def page2 = pages.get(1)
            assertThat(page2).isNotNull()
            assertThat(page2.getId()).isEqualTo(1L)
            assertThat(page2.getAuthors()).hasSize(1)
            verify(authorRepository, times(2)).save(authorArgumentCaptor.capture())
            def authors = authorArgumentCaptor.getAllValues()
            def author = authors.get(0)
            assertThat(author).isNotNull()
            assertThat(author.getId()).isNull()
            def author2 = authors.get(1)
            assertThat(author2).isNotNull()
            assertThat(author2.getId()).isEqualTo(2L)
            verify(messageRepository, only()).save(messageArgumentCaptor.capture())
            def message = messageArgumentCaptor.getValue()
            assertThat(message).isNotNull()
            assertThat(message.getId()).isNull()
        }
    }


    def "Should throw Exception when messages are not JSON"() {
        given:
        def allMessages = Set.of("this is not a JSON", "And this is also not one!")

        when:
        twitterMessageProcessor
                .processAllMessages(
                        allMessages,
                        1122333445566778899L,
                        998877665544332211L)
        then:
        def e = thrown(JsonProcessingException)
        e.message.contains("Unrecognized token 'this': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')")
    }


    def getMessageResource(String messageResource) throws IOException {
        final InputStream resourceAsStream1 = getClass().getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }
}
