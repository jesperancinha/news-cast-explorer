package org.jesperancinha.twitter.processor;

import org.apache.commons.io.IOUtils;
import org.jesperancinha.twitter.client.TwitterClient;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.db.Author;
import org.jesperancinha.twitter.model.db.Message;
import org.jesperancinha.twitter.model.db.Page;
import org.jesperancinha.twitter.repository.AuthorRepository;
import org.jesperancinha.twitter.repository.MessageRepository;
import org.jesperancinha.twitter.repository.PageRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TwitterMessageProcessorImplTest {

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

    @BeforeEach
    public void setUp() {
        final var testPage = Page.builder()
                .id(1L)
                .duration(2000L)
                .createdAt(1550265180555L)
                .build();
        final var testAuthor = Author.builder()
                .id(1L)
                .twitterAuthorId("1096517755875090433")
                .createdAt(1550265180556L)
                .nMessages(1L)
                .screenName("Author1ScreenName")
                .page(testPage)
                .name("Author1")
                .build();
        when(pageRepository.save(any(Page.class))).thenReturn(testPage);
        when(authorRepository.save(any(Author.class))).thenReturn(testAuthor);
        var testMessage = Message.builder().build();
        when(messageRepository.save(any(Message.class))).thenReturn(testMessage);
    }

    @Test
    void testProcessAllMessages_whenGoodMessage_OkParse() throws IOException, InterruptedException {
        final var resultExample1 = getMessageResource("/example1.json");
        final var allMessages = Set.of(resultExample1);

        final var pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(1);
        final var authorDto = pageDto.getAuthors().get(0);
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo("1096517755875090433");
        assertThat(authorDto.getName()).isEqualTo("Author1");
        assertThat(authorDto.getScreenName()).isEqualTo("Author1ScreenName");
        assertThat(authorDto.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto.getMessageDtos()).isNotNull();
        assertThat(authorDto.getMessageDtos()).hasSize(1);
        final var messageDto = authorDto.getMessageDtos().get(0);
        assertThat(messageDto).isNotNull();
        assertThat(messageDto.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto.getText()).isEqualTo("Message1");
        assertThat(messageDto.getCreatedAt()).isEqualTo(1578935617000L);

        verify(twitterClient, only()).startFetchProcess();
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
        assertThat(author2.getId()).isEqualTo(1);
        verify(messageRepository, only()).save(messageArgumentCaptor.capture());
    }

    @Test
    void testProcessAllMessages_when2Message1Author_OkParseOrdered() throws IOException {
        final String resultExample1 = getMessageResource("/example1.json");
        final String resultExample15 = getMessageResource("/example15.json");
        final Set<String> allMessages = Set.of(resultExample1, resultExample15);

        final PageDto pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(1);
        final AuthorDto authorDto = pageDto.getAuthors().get(0);
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo("1096517755875090433");
        assertThat(authorDto.getName()).isEqualTo("Author1");
        assertThat(authorDto.getScreenName()).isEqualTo("Author1ScreenName");
        assertThat(authorDto.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto.getMessageDtos()).isNotNull();
        assertThat(authorDto.getMessageDtos()).hasSize(2);
        final MessageDto messageDto1 = authorDto.getMessageDtos().get(0);
        assertThat(messageDto1).isNotNull();
        assertThat(messageDto1.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto1.getText()).isEqualTo("Message1");
        assertThat(messageDto1.getCreatedAt()).isEqualTo(1578935617000L);
        final MessageDto messageDto2 = authorDto.getMessageDtos().get(1);
        assertThat(messageDto2).isNotNull();
        assertThat(messageDto2.getId()).isEqualTo("1216771464755666944");
        assertThat(messageDto2.getText()).isEqualTo("Message2");
        assertThat(messageDto2.getCreatedAt()).isEqualTo(1578935898000L);
        assertThat(messageDto2.getCreatedAt()).isGreaterThan(messageDto1.getCreatedAt());
    }

    @Test
    void testProcessAllMessages_when2Message2Author_OkParseOrdered() throws IOException {
        final String resultExample1 = getMessageResource("/example1.json");
        final String resultExample2 = getMessageResource("/example2.json");
        final String resultExample3 = getMessageResource("/example3.json");
        final String resultExample15 = getMessageResource("/example15.json");
        final Set<String> allMessages = Set.of(resultExample1, resultExample15, resultExample3, resultExample2);

        final PageDto pageDto = twitterMessageProcessor.processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(3);
        final AuthorDto authorDto1 = pageDto.getAuthors().get(0);
        final AuthorDto authorDto2 = pageDto.getAuthors().get(1);
        final AuthorDto authorDto3 = pageDto.getAuthors().get(2);

        assertThat(authorDto1).isNotNull();
        assertThat(authorDto1.getId()).isEqualTo("3024323693");
        assertThat(authorDto1.getCreatedAt()).isEqualTo(1423362294000L);
        assertThat(authorDto1.getMessageDtos()).isNotNull();
        assertThat(authorDto1.getMessageDtos()).hasSize(1);

        assertThat(authorDto2).isNotNull();
        assertThat(authorDto2.getId()).isEqualTo("731044859927244800");
        assertThat(authorDto2.getCreatedAt()).isEqualTo(1463129651000L);
        assertThat(authorDto2.getMessageDtos()).isNotNull();
        assertThat(authorDto2.getMessageDtos()).hasSize(1);

        assertThat(authorDto3).isNotNull();
        assertThat(authorDto3.getId()).isEqualTo("1096517755875090433");
        assertThat(authorDto3.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto3.getMessageDtos()).isNotNull();
        assertThat(authorDto3.getMessageDtos()).hasSize(2);


        final var messageDto1 = authorDto1.getMessageDtos().get(0);
        assertThat(messageDto1).isNotNull();
        assertThat(messageDto1.getId()).isEqualTo("1216770650121084933");
        assertThat(messageDto1.getText()).isEqualTo("Message3");
        assertThat(messageDto1.getCreatedAt()).isEqualTo(1578935704000L);

        final var messageDto2 = authorDto2.getMessageDtos().get(0);
        assertThat(messageDto2).isNotNull();
        assertThat(messageDto2.getId()).isEqualTo("1216770915192668160");
        assertThat(messageDto2.getText()).isEqualTo("Message4");
        assertThat(messageDto2.getCreatedAt()).isEqualTo(1578935767000L);

        final var messageDto31 = authorDto3.getMessageDtos().get(0);
        assertThat(messageDto31).isNotNull();
        assertThat(messageDto31.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto31.getText()).isEqualTo("Message1");
        assertThat(messageDto31.getCreatedAt()).isEqualTo(1578935617000L);

        final var messageDto32 = authorDto3.getMessageDtos().get(1);
        assertThat(messageDto32).isNotNull();
        assertThat(messageDto32.getId()).isEqualTo("1216771464755666944");
        assertThat(messageDto32.getCreatedAt()).isEqualTo(1578935898000L);

        assertThat(messageDto32.getCreatedAt()).isGreaterThan(messageDto31.getCreatedAt());
        assertThat(authorDto3.getCreatedAt()).isGreaterThan(authorDto2.getCreatedAt()).isGreaterThan(authorDto1.getCreatedAt());
        assertThat(authorDto2.getCreatedAt()).isGreaterThan(authorDto1.getCreatedAt());
    }

    private String getMessageResource(String messageResource) throws IOException {
        final InputStream resourceAsStream1 = getClass().getResourceAsStream(messageResource);
        return IOUtils.toString(resourceAsStream1, Charset.defaultCharset());
    }
}