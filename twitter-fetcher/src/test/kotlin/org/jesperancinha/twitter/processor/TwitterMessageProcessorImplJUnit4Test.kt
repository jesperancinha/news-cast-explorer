package org.jesperancinha.twitter.processor;

import org.apache.commons.io.IOUtils;
import org.jesperancinha.twitter.client.TwitterClient;
import org.jesperancinha.twitter.model.db.Author;
import org.jesperancinha.twitter.model.db.Message;
import org.jesperancinha.twitter.model.db.Page;
import org.jesperancinha.twitter.repository.AuthorRepository;
import org.jesperancinha.twitter.repository.MessageRepository;
import org.jesperancinha.twitter.repository.PageRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TwitterMessageProcessorImplJUnit4Test {

    @Autowired
    private TwitterMessageProcessor twitterMessageProcessor;

    @MockBean
    private TwitterClient twitterClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private MessageRepository messageRepository;

    @MockBean
    private PageRepository pageRepository;

    @Captor
    private ArgumentCaptor<Message> messageArgumentCaptor;

    @Captor
    private ArgumentCaptor<Author> authorArgumentCaptor;

    @Captor
    private ArgumentCaptor<Page> pageArgumentCaptor;

    private static Page testPage;

    private static Author testAuthor;

    private static Message testMessage;

    @BeforeClass
    public static void beforeAll() {
        testPage = Page.builder()
                .id(1L)
                .duration(2000L)
                .createdAt(1550265180555L)
                .build();
        testAuthor = Author.builder()
                .id(2L)
                .twitterAuthorId("998877665544332211")
                .createdAt(1550265180556L)
                .nMessages(1L)
                .screenName("Author1ScreenName")
                .page(testPage)
                .name("Author1")
                .build();
        testMessage = Message.builder()
                .id(3L)
                .author(testAuthor)
                .twitterMessageId("1122333445566778899")
                .text("Message1")
                .createdAt(1550265180557L)
                .build();

    }

    @Before
    public void setUp() {
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);
        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);
    }

    @Test
    public void testProcessAllMessages_whenGoodMessage_OkParse() throws IOException, InterruptedException {
        final var resultExample1 = getMessageResource("/example1.json");
        final var allMessages = Set.of(resultExample1);

        final var pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(1);
        final var authorDto = pageDto.getAuthors().get(0);
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo("999999999000000000");
        assertThat(authorDto.getName()).isEqualTo("Author1");
        assertThat(authorDto.getScreenName()).isEqualTo("Author1ScreenName");
        assertThat(authorDto.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto.getMessageDtos()).isNotNull();
        assertThat(authorDto.getMessageDtos()).hasSize(1);
        final var messageDto = authorDto.getMessageDtos().get(0);
        assertThat(messageDto).isNotNull();
        assertThat(messageDto.getId()).isEqualTo("999999999000000000");
        assertThat(messageDto.getText()).isEqualTo("Message1");
        assertThat(messageDto.getCreatedAt()).isEqualTo(1578935617000L);

        verify(twitterClient, atMostOnce()).startFetchProcess();
        verify(pageRepository, times(2)).save(pageArgumentCaptor.capture());
        final var pages = pageArgumentCaptor.getAllValues();
        final var page = pages.get(0);
        assertThat(page).isNotNull();
        assertThat(page.getId()).isNull();
        assertThat(page.getAuthors()).isEmpty();
        final var page2 = pages.get(1);
        assertThat(page2).isNotNull();
        assertThat(page2.getId()).isEqualTo(1L);
        assertThat(page2.getAuthors()).hasSize(1);
        verify(authorRepository, times(2)).save(authorArgumentCaptor.capture());
        final var authors = authorArgumentCaptor.getAllValues();
        final var author = authors.get(0);
        assertThat(author).isNotNull();
        assertThat(author.getId()).isNull();
        final var author2 = authors.get(1);
        assertThat(author2).isNotNull();
        assertThat(author2.getId()).isEqualTo(2L);
        verify(messageRepository, only()).save(messageArgumentCaptor.capture());
        final var message = messageArgumentCaptor.getValue();
        assertThat(message).isNotNull();
        assertThat(message.getId()).isNull();
    }

    @Test
    @Ignore
    public void testProcessAllMessages_when2Message1Author_OkParseOrdered() throws IOException {

    }

    @Test
    @Ignore
    public void testProcessAllMessages_when2Message2Author_OkParseOrdered() throws IOException {
    }

    private String getMessageResource(String messageResource) throws IOException {
        final InputStream resourceAsStream1 = getClass().getResourceAsStream(messageResource);
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset());
    }

    @Test
    public void testMessages_whenMessageListInvalid_throwException()  {
        var allMessages = Set.of("this is not a JSON", "And this is also not one!");

//        assertThatExceptionOfType(
//                JsonSyntaxException.class)
//                .isThrownBy(() ->
//                        twitterMessageProcessor
//                                .processAllMessages(
//                                        allMessages,
//                                        1122333445566778899L,
//                                        998877665544332211L))
//                .withMessageContaining("Expected BEGIN_OBJECT but was STRING at line 1 column 1 path");
    }

    @After
    public void tearDown() {
        mockitoSession().initMocks(twitterClient);
    }
}