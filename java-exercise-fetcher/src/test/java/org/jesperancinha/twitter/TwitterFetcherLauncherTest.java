package org.jesperancinha.twitter;

import org.jesperancinha.twitter.client.TwitterClient;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.processor.TwitterMessageProcessor;
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

        final TwitterClient twitterClient
                = twitterFetcherLauncher.createClientFromArgs(args);

        assertThat(twitterClient).isNotNull();
        assertThat(twitterClient.getConsumerKey()).isEqualTo("consumerKey");
        assertThat(twitterClient.getConsumerSecret()).isEqualTo("consumerSecret");
        assertThat(twitterClient.getToken()).isEqualTo("token");
        assertThat(twitterClient.getTokenSecret()).isEqualTo("tokenSecret");
        assertThat(twitterClient.getSearchTerm()).isEqualTo("rogerfederer");
        assertThat(twitterClient.getCapacity()).isEqualTo(100);
        assertThat(twitterClient.getTimeToWaitSeconds()).isEqualTo(30);
        assertThat(twitterClient.getTwitterMessageProcessor())
                .isSameAs(TwitterMessageProcessor.getInstance());
    }

    @Test
    void testStartFetchingWithArguments_whenAllArguments_thenRunOk() throws InterruptedException {
        final String[] args = new String[]{
                "consumerKey", "consumerSecret", "token", "tokenSecret", "rogerfederer", "1", "2"
        };

        final TwitterClient twitterClient
                = twitterFetcherLauncher.createClientFromArgs(args);
        final PageDto pageDto = twitterClient.startFetchProcess();

        assertThat(twitterClient).isNotNull();
        assertThat(twitterClient.getConsumerKey()).isEqualTo("consumerKey");
        assertThat(twitterClient.getConsumerSecret()).isEqualTo("consumerSecret");
        assertThat(twitterClient.getToken()).isEqualTo("token");
        assertThat(twitterClient.getTokenSecret()).isEqualTo("tokenSecret");
        assertThat(twitterClient.getSearchTerm()).isEqualTo("rogerfederer");
        assertThat(twitterClient.getCapacity()).isEqualTo(1);
        assertThat(twitterClient.getTimeToWaitSeconds()).isEqualTo(2);
        assertThat(twitterClient.getTwitterMessageProcessor())
                .isSameAs(TwitterMessageProcessor.getInstance());

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