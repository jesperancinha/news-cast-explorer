package org.jesperancinha.twitter.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.Message;
import org.jesperancinha.twitter.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
public class TwitterClient {

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String tokenSecret;

    public TwitterClient(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public static void main(String[] args) throws InterruptedException {
        final String consumerKey = args[0];
        final String consumerSecret = args[1];
        final String token = args[2];
        final String tokenSecret = args[3];
        final TwitterClient twitterClient = new TwitterClient(consumerKey, consumerSecret, token, tokenSecret);
        twitterClient.startFetchProcess();
    }

    public PageDto startFetchProcess() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final List<String> allMessages = new ArrayList<>();
        final FetcherThread threadFetcher = new FetcherThread(consumerKey, consumerSecret, token, tokenSecret, allMessages,executorService);
        final KillerThread killerThread = new KillerThread(executorService);
        executorService.submit(threadFetcher);
        executorService.submit(killerThread);
        final long timestampBefore = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        final long timestampAfter = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        final List<AuthorDto> authorDtos = processAllMessages(allMessages);
        final PageDto pageDto = PageDto.builder().authors(authorDtos).duration(timestampAfter - timestampBefore).build();
        log.info(gson.toJson(pageDto));
        return pageDto;
    }

    private List<AuthorDto> processAllMessages(List<String> allMessages) {
        return allMessages.parallelStream()
                .map(message -> gson.fromJson(message, Message.class))
                .collect(twitterMessageCollector())
                .entrySet().stream()
                .map(TwitterClient::fillAuthor).sorted(Comparator.comparing(AuthorDto::getCreatedAt)).collect(Collectors.toList());
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.getUser()),
                Collectors.mapping(TwitterClient::toMessageDto, Collectors.toList()));
    }

    private static AuthorDto fillAuthor(Map.Entry<AuthorDto, List<MessageDto>> authorDtoListEntry) {
        AuthorDto key = authorDtoListEntry.getKey();
        List<MessageDto> listEntryValue = authorDtoListEntry.getValue();
        listEntryValue.sort(Comparator.comparing(MessageDto::getCreatedAt));
        key.setMessageDtos(listEntryValue);
        return key;
    }

    private static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .text(message.getText())
                .createdAt(message.getCreatedAt().getTime())
                .build();
    }

    private static AuthorDto toUserDto(User user) {
        return AuthorDto.builder()
                .id(user.getId())
                .name(user.getName())
                .createdAt(user.getCreatedAt().getTime())
                .screenName(user.getScreenName())
                .build();
    }
}
