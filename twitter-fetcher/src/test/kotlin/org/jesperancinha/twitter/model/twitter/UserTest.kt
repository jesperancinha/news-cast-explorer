package org.jesperancinha.twitter.model.twitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testEquals_whenEquals_Ok() throws JsonProcessingException {
        final User user1 = objectMapper.readValue(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate Alliance Switzerland\",\n" +
                        "      \"screen_name\": \"climalliancech\",\n" +
                        "      \"created_at\": \"Sun Apr 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        final User user2 = objectMapper.readValue(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate BALALLA Switzerland\",\n" +
                        "      \"screen_name\": \"UUUU\",\n" +
                        "      \"created_at\": \"Sun Jan 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        assertThat(user1).isEqualTo(user2);
    }

    @Test
    void testHashCode_whenOther_NotSame() throws JsonProcessingException {
        final User user1 = objectMapper.readValue(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate Alliance Switzerland\",\n" +
                        "      \"screen_name\": \"climalliancech\",\n" +
                        "      \"created_at\": \"Sun Apr 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        final User user2 = objectMapper.readValue(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate BALALLA Switzerland\",\n" +
                        "      \"screen_name\": \"UUUU\",\n" +
                        "      \"created_at\": \"Sun Jan 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        assertThat(user1).isNotSameAs(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }
}