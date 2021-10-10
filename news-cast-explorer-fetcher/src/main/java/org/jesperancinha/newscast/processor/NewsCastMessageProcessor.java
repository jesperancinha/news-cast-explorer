package org.jesperancinha.newscast.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jesperancinha.newscast.converters.AuthorConverter;
import org.jesperancinha.newscast.converters.MessageConverter;
import org.jesperancinha.newscast.converters.NewsCastDtoConverter;
import org.jesperancinha.newscast.converters.PageConverter;
import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.model.source.Message;
import org.jesperancinha.newscast.repository.AuthorRepository;
import org.jesperancinha.newscast.repository.MessageRepository;
import org.jesperancinha.newscast.repository.PageRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.jesperancinha.newscast.converters.NewsCastDtoConverter.toUserDto;

@Slf4j
@Component
@Builder
public class NewsCastMessageProcessor {

    private final MessageRepository messageRepository;

    private final AuthorRepository authorRepository;

    private final PageRepository pageRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private NewsCastMessageProcessor(MessageRepository messageRepository, AuthorRepository authorRepository, PageRepository pageRepository) {
        this.messageRepository = messageRepository;
        this.authorRepository = authorRepository;
        this.pageRepository = pageRepository;
    }

    public PageDto processAllMessages(Set<String> allMessages, Long timestampBefore, Long timestampAfter) throws JsonProcessingException {
        final var list = new ArrayList<JsonProcessingException>();
        final var authorDtos = allMessages.parallelStream()
                .map(message -> {
                    try {
                        return objectMapper.readValue(message, Message.class);
                    } catch (JsonProcessingException e) {
                        list.add(e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(twitterMessageCollector())
                .entrySet().stream()
                .map(NewsCastMessageProcessor::fillAuthor)
                .sorted(Comparator.comparing(AuthorDto::createdAt))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            throw list.stream().min((o1, o2) -> o2.getMessage().compareTo(o1.getMessage())).orElse(null);
        }
        final var pageDto = PageDto.builder().createdAt(timestampBefore).authors(authorDtos).duration(timestampAfter - timestampBefore).build();
        savePageLog(pageDto);
        savePageDb(pageDto);
        return pageDto;
    }

    private void savePageLog(PageDto pageDto) throws JsonProcessingException {
        log.info(objectMapper.writeValueAsString(pageDto));
    }

    private void savePageDb(PageDto pageDto) {
        var page = pageRepository.save(PageConverter.toData(pageDto));
        for (var authorDto : pageDto.authors()) {
            var authorToSave = AuthorConverter.toData(authorDto, page);
            page.getAuthors().add(authorToSave);
            authorToSave.setPage(pageRepository.save(page));
            var author = authorRepository.save(authorToSave);
            authorDto.messageDtos()
                    .forEach(messageDto -> {
                        var message = MessageConverter.toData(messageDto);
                        var save = messageRepository.save(message);
                        author.getMessages().add(save);
                        authorRepository.save(author);
                    });
        }
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.user()),
                Collectors.mapping(NewsCastDtoConverter::toMessageDto, Collectors.toList()));
    }

    private static AuthorDto fillAuthor(Map.Entry<AuthorDto, List<MessageDto>> authorDtoListEntry) {
        var authorDto = authorDtoListEntry.getKey();
        var listEntryValue = authorDtoListEntry.getValue();
        listEntryValue.sort(Comparator.comparing(MessageDto::createdAt));
        return AuthorDto.builder()
                .id(authorDto.id())
                .createdAt(authorDto.createdAt())
                .name(authorDto.name())
                .screenName(authorDto.screenName())
                .messageDtos(listEntryValue)
                .nMessages(listEntryValue.size())
                .build();
    }

}
