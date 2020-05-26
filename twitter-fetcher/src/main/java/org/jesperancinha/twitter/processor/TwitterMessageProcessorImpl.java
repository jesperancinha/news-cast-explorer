package org.jesperancinha.twitter.processor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.twitter.Message;
import org.jesperancinha.twitter.model.twitter.User;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TwitterMessageProcessorImpl implements TwitterMessageProcessor {

    private static final TwitterMessageProcessorImpl twitterMessageProcessorImpl;

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    static {
        twitterMessageProcessorImpl = new TwitterMessageProcessorImpl();
    }

    private TwitterMessageProcessorImpl() {
    }

    public static TwitterMessageProcessorImpl getInstance() {
        return twitterMessageProcessorImpl;
    }

    public PageDto processAllMessages(Set<String> allMessages, Long timestampBefore, Long timestampAfter) {
        List<AuthorDto> authorDtos = allMessages.parallelStream()
                .map(message -> gson.fromJson(message, Message.class))
                .collect(twitterMessageCollector())
                .entrySet().stream()
                .map(TwitterMessageProcessorImpl::fillAuthor).sorted(Comparator.comparing(AuthorDto::getCreatedAt)).collect(Collectors.toList());
        final PageDto pageDto = PageDto.builder().createdAt(timestampBefore).authors(authorDtos).duration(timestampAfter - timestampBefore).build();
        log.info(gson.toJson(pageDto));
        return pageDto;
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.user()),
                Collectors.mapping(TwitterMessageProcessorImpl::toMessageDto, Collectors.toList()));
    }

    private static AuthorDto fillAuthor(Map.Entry<AuthorDto, List<MessageDto>> authorDtoListEntry) {
        AuthorDto authorDto = authorDtoListEntry.getKey();
        List<MessageDto> listEntryValue = authorDtoListEntry.getValue();
        listEntryValue.sort(Comparator.comparing(MessageDto::getCreatedAt));
        authorDto.setMessageDtos(listEntryValue);
        authorDto.setNMessages((long) listEntryValue.size());
        return authorDto;
    }

    private static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.id())
                .text(message.text())
                .createdAt(message.createdAt().getTime())
                .build();
    }

    private static AuthorDto toUserDto(User user) {
        return AuthorDto.builder()
                .id(user.id())
                .name(user.name())
                .createdAt(user.createdAt().getTime())
                .screenName(user.screenName())
                .nMessages(0L)
                .build();
    }


}
