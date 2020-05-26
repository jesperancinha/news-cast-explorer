package org.jesperancinha.twitter.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jesperancinha.twitter.model.twitter.Message;
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
                        "  \"text\": \"RT @climalliancech: Congrats @RogerFederer for your declaration #RogerForClimate!\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Fossil Free Penn\"" +
                        "  }" +
                        "}", Message.class);

        final Message message2 = gson.fromJson(
                "{\n" +
                        "  \"created_at\": \"Mon Jan 13 17:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"RT @Climaaaaaaaate: Congrats @RogerFederer for your declaration #RogerForClimate!\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Fossil Spree Penn\",\n" +
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
                        "  \"text\": \"RT @climalliancech: Congrats @RogerFederer for your declaration #RogerForClimate!\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Fossil Free Penn\"" +
                        "  }" +
                        "}", Message.class);

        final Message message2 = gson.fromJson(
                "{\n" +
                        "  \"created_at\": \"Mon Jan 13 17:15:04 +0000 2020\",\n" +
                        "  \"id\": 1216770650121084933,\n" +
                        "  \"id_str\": \"1216770650121084933\",\n" +
                        "  \"text\": \"RT @Climaaaaaaaate: Congrats @RogerFederer for your declaration #RogerForClimate!\",\n" +
                        "  \"user\": {\n" +
                        "    \"id\": 3024323693,\n" +
                        "    \"name\": \"Fossil Spree Penn\",\n" +
                        "    \"notifications\": null\n" +
                        "  }}", Message.class);

        assertThat(message1).isNotSameAs(message2);
        assertThat(message1.hashCode()).isEqualTo(message2.hashCode());
    }
}