package org.jesperancinha.twitter;

import org.jesperancinha.twitter.client.TwitterClient;
import org.jesperancinha.twitter.client.TwitterClientImpl;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.processor.TwitterMessageProcessorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TwitterFetcherLauncherTest {

    @InjectMocks
    private TwitterFetcherLauncher twitterFetcherLauncher;

    @Mock
    private TwitterClient twitterClient;

    @Test
    void testStartFetchingWithArguments_whenArguments_thenRunOkWithDefaults() throws InterruptedException {
        final String[] args = new String[]{
                "consumerKey", "consumerSecret", "token", "tokenSecret", "rogerfederer"
        };

        final TwitterClientImpl twitterClientImpl
                = twitterFetcherLauncher.createClientFromArgs(args);

        assertThat(twitterClientImpl).isNotNull();
        assertThat(twitterClientImpl.getConsumerKey()).isEqualTo("consumerKey");
        assertThat(twitterClientImpl.getConsumerSecret()).isEqualTo("consumerSecret");
        assertThat(twitterClientImpl.getToken()).isEqualTo("token");
        assertThat(twitterClientImpl.getTokenSecret()).isEqualTo("tokenSecret");
        assertThat(twitterClientImpl.getSearchTerm()).isEqualTo("rogerfederer");
        assertThat(twitterClientImpl.getCapacity()).isEqualTo(100);
        assertThat(twitterClientImpl.getTimeToWaitSeconds()).isEqualTo(30);
        assertThat(twitterClientImpl.getTwitterMessageProcessor())
                .isSameAs(TwitterMessageProcessorImpl.getInstance());
    }

    @Test
    void testStartFetchingWithArguments_whenAllArguments_thenRunOk() throws InterruptedException {
        final String[] args = new String[]{
                "consumerKey", "consumerSecret", "token", "tokenSecret", "rogerfederer", "1", "2"
        };

        final TwitterClientImpl twitterClientImpl
                = twitterFetcherLauncher.createClientFromArgs(args);
        final PageDto pageDto = twitterClientImpl.startFetchProcess();

        assertThat(twitterClientImpl).isNotNull();
        assertThat(twitterClientImpl.getConsumerKey()).isEqualTo("consumerKey");
        assertThat(twitterClientImpl.getConsumerSecret()).isEqualTo("consumerSecret");
        assertThat(twitterClientImpl.getToken()).isEqualTo("token");
        assertThat(twitterClientImpl.getTokenSecret()).isEqualTo("tokenSecret");
        assertThat(twitterClientImpl.getSearchTerm()).isEqualTo("rogerfederer");
        assertThat(twitterClientImpl.getCapacity()).isEqualTo(1);
        assertThat(twitterClientImpl.getTimeToWaitSeconds()).isEqualTo(2);
        assertThat(twitterClientImpl.getTwitterMessageProcessor())
                .isSameAs(TwitterMessageProcessorImpl.getInstance());

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isGreaterThan(0);
        assertThat(pageDto.getCreatedAt()).isGreaterThanOrEqualTo(0);
        final List<AuthorDto> authors = pageDto.getAuthors();
        assertThat(authors).isNotNull();
        assertThat(authors).isEmpty();
    }

    @Test
    void testStartFetchingWithArguments_whenRunAllArguments_thenRunOk() throws InterruptedException {
        final String[] args = new String[]{
                "consumerKey", "consumerSecret", "token", "tokenSecret", "rogerfederer", "1", "2"
        };

        assertAll(() -> twitterFetcherLauncher.run(args));
    }

    @Test
    void testStartFetchingWithArguments_whenMainAllArguments_thenRunOk() throws InterruptedException {
        final String[] args = new String[]{
                "consumerKey", "consumerSecret", "token", "tokenSecret", "rogerfederer", "1", "2"
        };

        assertAll(() -> TwitterFetcherLauncher.main(args));
    }

    @Test
    void testScheduled_whenRun_thenCallsRightMethod() throws InterruptedException {
        twitterFetcherLauncher.scheduled();

        verify(twitterClient, only()).startFetchProcess();
    }
}