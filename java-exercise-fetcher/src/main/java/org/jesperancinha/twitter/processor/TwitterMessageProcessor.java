package org.jesperancinha.twitter.processor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.Message;
import org.jesperancinha.twitter.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
public class TwitterMessageProcessor {

    private static TwitterMessageProcessor twitterMessageProcessor;

    static {
        twitterMessageProcessor = new TwitterMessageProcessor();
    }

    private TwitterMessageProcessor() {
    }

    public static TwitterMessageProcessor getInstance() {
        return twitterMessageProcessor;
    }

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public PageDto processAllMessages(List<String> allMessages, long timestampBefore, long timestampAfter) {
        List<AuthorDto> authorDtos = allMessages.parallelStream()
                .map(message -> gson.fromJson(message, Message.class))
                .filter(message -> Objects.nonNull(message.getUser()))
                .collect(twitterMessageCollector())
                .entrySet().stream()
                .map(TwitterMessageProcessor::fillAuthor).sorted(Comparator.comparing(AuthorDto::getCreatedAt)).collect(Collectors.toList());
        final PageDto pageDto = PageDto.builder().createdAt(timestampBefore).authors(authorDtos).duration(timestampAfter - timestampBefore).build();
        log.info(gson.toJson(pageDto));
        return pageDto;
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.getUser()),
                Collectors.mapping(TwitterMessageProcessor::toMessageDto, Collectors.toList()));
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
