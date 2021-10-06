package org.jesperancinha.newscast.model.source;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

/**
 * We use records for twitter because we are using serialization for it
 * They are not only handy, but in fact needed.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
        String id,
        @JsonFormat
                (shape = JsonFormat.Shape.STRING,
                        pattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy")
        @JsonProperty("created_at") Date createdAt,
        String name,
        @JsonProperty("screen_name")
        String screenName
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
