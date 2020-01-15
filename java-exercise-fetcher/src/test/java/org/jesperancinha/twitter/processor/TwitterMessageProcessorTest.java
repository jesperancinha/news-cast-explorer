package org.jesperancinha.twitter.processor;

import org.apache.commons.io.IOUtils;
import org.jesperancinha.twitter.data.AuthorDto;
import org.jesperancinha.twitter.data.MessageDto;
import org.jesperancinha.twitter.data.PageDto;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitterMessageProcessorTest {

    @Test
    void testProcessAllMessages_whenGoodMessage_OkParse() throws IOException {
        final String resultExample1 = getMessageResource("/example1.json");
        final List<String> allMessages = List.of(resultExample1);

        final PageDto pageDto = TwitterMessageProcessor.getInstance().processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(1);
        final AuthorDto authorDto = pageDto.getAuthors().get(0);
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo("1096517755875090433");
        assertThat(authorDto.getName()).isEqualTo("Loukina");
        assertThat(authorDto.getScreenName()).isEqualTo("loukinatille");
        assertThat(authorDto.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto.getMessageDtos()).isNotNull();
        assertThat(authorDto.getMessageDtos()).hasSize(1);
        final MessageDto messageDto = authorDto.getMessageDtos().get(0);
        assertThat(messageDto).isNotNull();
        assertThat(messageDto.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto.getText()).isEqualTo("RT Federer is da Best!");
        assertThat(messageDto.getCreatedAt()).isEqualTo(1578935617000L);
    }

    @Test
    void testProcessAllMessages_when2Message1Author_OkParseOrdered() throws IOException {
        final String resultExample1 = getMessageResource("/example1.json");
        final String resultExample15 = getMessageResource("/example15.json");
        final List<String> allMessages = List.of(resultExample1, resultExample15);

        final PageDto pageDto = TwitterMessageProcessor.getInstance().processAllMessages(allMessages, 1579079712000L, 1579079714000L);

        assertThat(pageDto).isNotNull();
        assertThat(pageDto.getDuration()).isEqualTo(2000L);
        assertThat(pageDto.getAuthors()).hasSize(1);
        final AuthorDto authorDto = pageDto.getAuthors().get(0);
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getId()).isEqualTo("1096517755875090433");
        assertThat(authorDto.getName()).isEqualTo("Loukina");
        assertThat(authorDto.getScreenName()).isEqualTo("loukinatille");
        assertThat(authorDto.getCreatedAt()).isEqualTo(1550265180000L);
        assertThat(authorDto.getMessageDtos()).isNotNull();
        assertThat(authorDto.getMessageDtos()).hasSize(2);
        final MessageDto messageDto1 = authorDto.getMessageDtos().get(0);
        assertThat(messageDto1).isNotNull();
        assertThat(messageDto1.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto1.getText()).isEqualTo("RT Federer is da Best!");
        assertThat(messageDto1.getCreatedAt()).isEqualTo(1578935617000L);
        final MessageDto messageDto2 = authorDto.getMessageDtos().get(1);
        assertThat(messageDto2).isNotNull();
        assertThat(messageDto2.getId()).isEqualTo("1216771464755666944");
        assertThat(messageDto2.getText()).isEqualTo("RT Il est Roger d\u00e9, Roger \u00e9Federer. Super. C'est Le Best!\u2026e");
        assertThat(messageDto2.getCreatedAt()).isEqualTo(1578935898000L);
        assertThat(messageDto2.getCreatedAt()).isGreaterThan(messageDto1.getCreatedAt());
    }

    @Test
    void testProcessAllMessages_when2Message2Author_OkParseOrdered() throws IOException {
        final String resultExampe1 = getMessageResource("/example1.json");
        final String resultExampe2 = getMessageResource("/example2.json");
        final String resultExampe3 = getMessageResource("/example3.json");
        final String resultExampe15 = getMessageResource("/example15.json");
        final List<String> allMessages = List.of(resultExampe1, resultExampe15, resultExampe3, resultExampe2);

        final PageDto pageDto = TwitterMessageProcessor.getInstance().processAllMessages(allMessages, 1579079712000L, 1579079714000L);

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


        final MessageDto messageDto1 = authorDto1.getMessageDtos().get(0);
        assertThat(messageDto1).isNotNull();
        assertThat(messageDto1.getId()).isEqualTo("1216770650121084933");
        assertThat(messageDto1.getText()).isEqualTo("RT @climalliancech: Congrats @RogerFederer for your declaration #RogerForClimate!");
        assertThat(messageDto1.getCreatedAt()).isEqualTo(1578935704000L);

        final MessageDto messageDto2 = authorDto2.getMessageDtos().get(0);
        assertThat(messageDto2).isNotNull();
        assertThat(messageDto2.getId()).isEqualTo("1216770915192668160");
        assertThat(messageDto2.getText()).isEqualTo("RT @XRZurich: Gratulation @rogerfederer für die Aufgabe deiner Werbeverträge mit Banken ( @CreditSuisse ), welche mit ihren Investitionen d…");
        assertThat(messageDto2.getCreatedAt()).isEqualTo(1578935767000L);

        final MessageDto messageDto31 = authorDto3.getMessageDtos().get(0);
        assertThat(messageDto31).isNotNull();
        assertThat(messageDto31.getId()).isEqualTo("1216770288601436165");
        assertThat(messageDto31.getText()).isEqualTo("RT Federer is da Best!");
        assertThat(messageDto31.getCreatedAt()).isEqualTo(1578935617000L);

        final MessageDto messageDto32 = authorDto3.getMessageDtos().get(1);
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