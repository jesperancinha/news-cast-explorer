package org.jesperancinha.twitter.model;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jesperancinha.twitter.model.twitter.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private final static Gson gson = new GsonBuilder()
            .setDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy")
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();


    @Test
    void testEquals_whenEquals_Ok() {
        final User user1 = gson.fromJson(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate Alliance Switzerland\",\n" +
                        "      \"screen_name\": \"climalliancech\",\n" +
                        "      \"created_at\": \"Sun Apr 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        final User user2 = gson.fromJson(
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
    void testHashCode_whenOther_NotSame() {
        final User user1 = gson.fromJson(
                "{\n" +
                        "      \"id\": 988105075267637248,\n" +
                        "      \"id_str\": \"988105075267637248\",\n" +
                        "      \"name\": \"Climate Alliance Switzerland\",\n" +
                        "      \"screen_name\": \"climalliancech\",\n" +
                        "      \"created_at\": \"Sun Apr 22 17:19:42 +0000 2018\"\n" +
                        "    }", User.class);

        final User user2 = gson.fromJson(
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