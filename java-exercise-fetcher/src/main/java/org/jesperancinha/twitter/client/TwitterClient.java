package org.jesperancinha.twitter.client;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.model.Message;
import org.jesperancinha.twitter.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class TwitterClient {

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        List<String> allMessages = new ArrayList<>();
        FetcherThread threadFetcher = new FetcherThread(
                "",
                "",
                "-",
                "", allMessages);
        KillerThread killerThread = new KillerThread(service);
        service.submit(threadFetcher);
        service.submit(killerThread);
        service.shutdown();
        service.awaitTermination(30, TimeUnit.SECONDS);

        Map<AuthorDto, List<MessageDto>> collect = allMessages.parallelStream()
                .map(message -> gson.fromJson(message, Message.class))
                .sorted(Comparator.comparing(o -> o.getUser().getId()))
                .collect(Collectors.groupingBy(message -> toUserDto(message.getUser()),
                        Collectors.mapping(TwitterClient::toMessageDto, Collectors.toList())));

        LinkedHashMap<AuthorDto, List<MessageDto>> collect1 = collect.entrySet().stream()
                .sorted(Comparator.comparing(o -> o.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
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
