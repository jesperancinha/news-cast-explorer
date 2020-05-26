package org.jesperancinha.twitter.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Builder
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author {

    @Id
    private Long id;

    private String twitterAuthorId;

    private Long createdAt;

    private String name;

    private String screenName;

    @OneToMany(mappedBy = "author")
    private List<Message> messages;

    private Long nMessages;

    @ManyToOne(optional = false,
            cascade = ALL)
    @JoinColumn(name = "page_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "id")
    private Page page;
}

