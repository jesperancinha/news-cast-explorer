package org.jesperancinha.newscast.model.explorer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String newsCastAuthorId;

    private Long createdAt;

    private String name;

    private String userName;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private final List<Message> messages = new ArrayList<>();

    private Integer nMessages;

    @ManyToOne(optional = false)
    @JoinColumn(name = "page_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "id")
    private Page page;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return id != null && Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

