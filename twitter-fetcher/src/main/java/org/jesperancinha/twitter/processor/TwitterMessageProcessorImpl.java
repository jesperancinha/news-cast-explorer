package org.jesperancinha.twitter.processor;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.twitter.converters.AuthorConverter;
import org.jesperancinha.twitter.converters.MessageConverter;
import org.jesperancinha.twitter.converters.PageConverter;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.jesperancinha.twitter.model.twitter.Message;
import org.jesperancinha.twitter.model.twitter.User;
import org.jesperancinha.twitter.repository.AuthorRepository;
import org.jesperancinha.twitter.repository.MessageRepository;
import org.jesperancinha.twitter.repository.PageRepository;
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


    private final MessageRepository messageRepository;

    private final AuthorRepository authorRepository;

    private final PageRepository pageRepository;

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();


    private TwitterMessageProcessorImpl(MessageRepository messageRepository, AuthorRepository authorRepository, PageRepository pageRepository) {
        this.messageRepository = messageRepository;
        this.authorRepository = authorRepository;
        this.pageRepository = pageRepository;
    }

    public PageDto processAllMessages(Set<String> allMessages, Long timestampBefore, Long timestampAfter) {
        final var authorDtos = allMessages.parallelStream()
                .map(message -> gson.fromJson(message, Message.class))
                .collect(twitterMessageCollector())
                .entrySet().stream()
                .map(TwitterMessageProcessorImpl::fillAuthor).sorted(Comparator.comparing(AuthorDto::getCreatedAt)).collect(Collectors.toList());
        final var pageDto = PageDto.builder().createdAt(timestampBefore).authors(authorDtos).duration(timestampAfter - timestampBefore).build();
        savePageLog(pageDto);
        savePageDb(pageDto);
        return pageDto;
    }

    private void savePageLog(PageDto pageDto) {
        log.info(gson.toJson(pageDto));
    }

    private void savePageDb(PageDto pageDto) {
        var page = pageRepository.save(PageConverter.toData(pageDto));
        for (var authorDto : pageDto.getAuthors()) {
            var authorToSave = AuthorConverter.toData(authorDto, page);
            page.getAuthors().add(authorToSave);
            authorToSave.setPage(pageRepository.save(page));
            var author = authorRepository.save(authorToSave);
            authorDto.getMessageDtos()
                    .forEach(messageDto -> {
                        var message = MessageConverter.toData(messageDto, author);
                        var save = messageRepository.save(message);
                        author.getMessages().add(save);
                        authorRepository.save(author);
                    });
        }
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.user()),
                Collectors.mapping(TwitterMessageProcessorImpl::toMessageDto, Collectors.toList()));
    }

    private static AuthorDto fillAuthor(Map.Entry<AuthorDto, List<MessageDto>> authorDtoListEntry) {
        var authorDto = authorDtoListEntry.getKey();
        var listEntryValue = authorDtoListEntry.getValue();
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
