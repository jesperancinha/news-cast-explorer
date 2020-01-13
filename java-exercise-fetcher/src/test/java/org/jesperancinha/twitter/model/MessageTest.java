package org.jesperancinha.twitter.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {
    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Test
    public void testMessage_whenParsing_ResultOk() {
        final Message message = gson.fromJson(new InputStreamReader(getClass().getResourceAsStream("/example1.json")), Message.class);

        assertThat(message).isNotNull();
    }
}