package org.jesperancinha.twitter.model.twitter;

import java.util.Date;
import java.util.Objects;

/**
 * We use records for twitter because we are using serialization for it
 * They are not only handy, but in fact needed.
 */
public record User(
        String id,
        Date createdAt,
        String name,
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
