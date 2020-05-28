package org.jesperancinha.twitter.model.twitter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();


    @Test
    public void testEquals_whenEquals_Ok() {
        final Message message1 = gson.fromJson(
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

        final Message message2 = gson.fromJson(
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
    public void testHashCode_whenOther_NotSame() {
        final Message message1 = gson.fromJson(
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

        final Message message2 = gson.fromJson(
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