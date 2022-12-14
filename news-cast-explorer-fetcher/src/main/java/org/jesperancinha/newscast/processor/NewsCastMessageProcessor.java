package org.jesperancinha.newscast.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jesperancinha.newscast.converters.AuthorConverter;
import org.jesperancinha.newscast.converters.MessageConverter;
import org.jesperancinha.newscast.converters.NewsCastDtoConverter;
import org.jesperancinha.newscast.converters.PageConverter;
import org.jesperancinha.newscast.data.AuthorDto;
import org.jesperancinha.newscast.data.MessageDto;
import org.jesperancinha.newscast.data.PageDto;
import org.jesperancinha.newscast.model.explorer.Author;
import org.jesperancinha.newscast.model.explorer.Page;
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
import java.util.concurrent.atomic.AtomicReference;
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
        val list = new ArrayList<JsonProcessingException>();
        val authorDtos = allMessages.parallelStream()
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
        val entity = PageConverter.toData(pageDto);
        val page = pageRepository.save(entity);
        for (var authorDto : pageDto.authors()) {
            Author author = authorRepository.findFirstByNewsCastAuthorIdAndPageId(authorDto.newsCastId(), page.getId());
            author = createAuthor(page, authorDto, author);
            val authorRef = new AtomicReference<>(author);
            authorDto.messages()
                    .forEach(messageDto -> {
                        var message = messageRepository.findFirstByNewscastMessageIdAndAuthorId(messageDto.newsCastId(), authorRef.get().getId());
                        if (message == null) {
                            var save = messageRepository.save(MessageConverter.toData(messageDto, authorRef.get()));
                            authorRef.get().getMessages().add(save);
                            authorRef.set(authorRepository.save(authorRef.get()));
                        }
                    });
        }
    }

    private Author createAuthor(Page page, AuthorDto authorDto, Author author) {
        var authorToSave = author;
        if (author == null) {
            authorToSave = AuthorConverter.toData(authorDto, page);
            author = authorRepository.save(authorToSave);
            page.getAuthors().add(author);
            authorToSave.setPage(pageRepository.save(page));
        }
        return author;
    }

    private static Collector<Message, ?, Map<AuthorDto, List<MessageDto>>> twitterMessageCollector() {
        return Collectors.groupingBy(
                message -> toUserDto(message.user()),
                Collectors.mapping(NewsCastDtoConverter::toMessageDto, Collectors.toList()));
    }

    private static AuthorDto fillAuthor(Map.Entry<AuthorDto, List<MessageDto>> authorDtoListEntry) {
        val authorDto = authorDtoListEntry.getKey();
        val listEntryValue = authorDtoListEntry.getValue();
        listEntryValue.sort(Comparator.comparing(MessageDto::createdAt));
        return AuthorDto.builder()
                .newsCastId(authorDto.newsCastId())
                .createdAt(authorDto.createdAt())
                .name(authorDto.name())
                .userName(authorDto.userName())
                .messages(listEntryValue)
                .build();
    }

}
