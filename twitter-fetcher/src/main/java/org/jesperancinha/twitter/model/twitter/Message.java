package org.jesperancinha.twitter.model.twitter;

import java.util.Date;
import java.util.Objects;


/**
 * We use records for twitter because we are using serialization for it
 * They are not only handy, but in fact needed.
 */
public record Message(
        String id,
        Date createdAt,
        String text,
        User user
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
