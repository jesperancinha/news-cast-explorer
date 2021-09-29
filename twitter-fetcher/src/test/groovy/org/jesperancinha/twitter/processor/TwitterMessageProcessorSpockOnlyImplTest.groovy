package org.jesperancinha.twitter.processor

import com.fasterxml.jackson.core.JsonProcessingException
import org.apache.commons.io.IOUtils
import org.jesperancinha.twitter.client.TwitterClient
import org.jesperancinha.twitter.model.db.Author
import org.jesperancinha.twitter.model.db.Message
import org.jesperancinha.twitter.model.db.Page
import org.jesperancinha.twitter.repository.AuthorRepository
import org.jesperancinha.twitter.repository.MessageRepository
import org.jesperancinha.twitter.repository.PageRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.nio.charset.Charset

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class TwitterMessageProcessorSpockOnlyImplTest extends Specification {
    @Autowired
    private TwitterMessageProcessor twitterMessageProcessor

    @SpringBean
    private TwitterClient twitterClient = Mock()

    @SpringBean
    private AuthorRepository authorRepository = Mock()

    @SpringBean
    private MessageRepository messageRepository = Mock()

    @SpringBean
    private PageRepository pageRepository = Mock()

    private static Page testPage
    private static Page testPageFirstSave

    private static Author testAuthor
    private static Author testAuthorFirstSave

    private static Message testMessage

    void setupSpec() {
        testPage = Page.builder()
                .id(1L)
                .duration(2000L)
                .createdAt(1550265180555L)
                .build()
        testPageFirstSave = Page.builder()
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
        testAuthorFirstSave = Author.builder()
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
                .twitterMessageId("1122333445566778899")
                .text("Message1")
                .createdAt(1550265180557L)
                .build()
    }

    void setup() {
    }

    void cleanup() {
    }

    def "Should process messages with example 1"() {
        given:
        def resultExample1 = getMessageResource("/example1.json")
        def allMessages = Set.of(resultExample1)
        pageRepository.save(_) >> testPage
        authorRepository.save(_) >> testAuthor
        messageRepository.save(_) >> testMessage
        List<Page> pages = new ArrayList<>()
        List<Author> authors = new ArrayList<>()

        when:
        def pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L)

        then:
        pageDto != null
        pageDto.getDuration() == 2000L
        pageDto.getAuthors().size() == 1
        def authorDto = pageDto.getAuthors().get(0)
        authorDto != null
        authorDto.getId() == "206"
        authorDto.getName() == "lola_montes"
        authorDto.getScreenName() == "bacalhau_oil"
        authorDto.getCreatedAt() == 1632872907000L
        authorDto.getMessageDtos() != null
        authorDto.getMessageDtos().size() == 1
        def messageDto = authorDto.getMessageDtos().get(0)
        messageDto != null
        messageDto.getId() == "195"
        messageDto.getText() == "tuna california reaper mint beef sugar cod fish salt naga jolokia tuna parsley"
        messageDto.getCreatedAt() == 1632872907000L

        (0..1) * twitterClient.startFetchProcess()
        2 * pageRepository.save(_ as Page) >> { args ->
            Page page = args[0] as Page
            pages.add(page)
            return testPageFirstSave
        }
        def page1 = pages.getAt(0)
        page1 != null
        page1.getId() == null
        page1.getAuthors().isEmpty()
        def page2 = pages.getAt(1)
        page2 != null
        page2.getId() == 1L
        page2.getAuthors().size() == 1
        2 * authorRepository.save(_ as Author) >> { args ->
            def author = args[0] as Author
            authors.add(author)
            return testAuthorFirstSave
        }
        def author = authors.get(0)
        author != null
        author.getId() == null
        def author2 = authors.get(1)
        author2 != null
        author2.getId() == 2L
        1 * messageRepository.save(_ as Message) >> { args ->
            def message = args[0] as Message
            message != null
            message.getId() != null
            return message
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
        e.message != null
    }


    def getMessageResource(String messageResource) throws IOException {
        final InputStream resourceAsStream1 = getClass().getResourceAsStream(messageResource)
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset())
    }
}
