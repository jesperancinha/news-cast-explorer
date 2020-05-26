package org.jesperancinha.twitter.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.GenerationType.AUTO;

@Builder
@EqualsAndHashCode
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Author {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String twitterAuthorId;

    private Long createdAt;

    private String name;

    private String screenName;

    @OneToMany(mappedBy = "author",
            cascade = CascadeType.MERGE)
    private final List<Message> messages = new ArrayList<>();

    private Long nMessages;

    @ManyToOne(optional = false)
    @JoinColumn(name = "page_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "id")
    private Page page;
}

