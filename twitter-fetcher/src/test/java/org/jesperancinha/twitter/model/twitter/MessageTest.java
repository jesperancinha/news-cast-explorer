package org.jesperancinha.twitter.model.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testEquals_whenEquals_Ok() throws JsonProcessingException {
        final Message message1 = objectMapper.readValue(
                "{\n" +
                        "  \"created_at\": \"Mon Feb 13 12:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"Message3\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Author2\"" +
                        "  }" +
                        "}", Message.class);

        final Message message2 = objectMapper.readValue(
                "{\n" +
                        "  \"created_at\": \"Mon Jan 13 17:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"Message5\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Author3\",\n" +
                        "    \"notifications\": null\n" +
                        "  }}", Message.class);

        assertThat(message1).isEqualTo(message2);
    }

    @Test
    public void testHashCode_whenOther_NotSame() throws JsonProcessingException {
        final Message message1 = objectMapper.readValue(
                "{\n" +
                        "  \"created_at\": \"Mon Feb 13 12:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"Message3\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Author2\"" +
                        "  }" +
                        "}", Message.class);

        final Message message2 = objectMapper.readValue(
                "{\n" +
                        "  \"created_at\": \"Mon Jan 13 17:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"Message5\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Author3\",\n" +
                        "    \"notifications\": null\n" +
                        "  }}", Message.class);

        assertThat(message1).isNotSameAs(message2);
        assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
    }
}