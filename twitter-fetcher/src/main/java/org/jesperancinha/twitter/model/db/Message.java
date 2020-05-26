package org.jesperancinha.twitter.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.ALL;

@Builder
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    private Long id;

    private String twitterMessageId;

    @EqualsAndHashCode.Exclude
    private Long createdAt;

    @EqualsAndHashCode.Exclude
    private String text;

    @ManyToOne(optional = false,
            cascade = ALL)
    @JoinColumn(name = "author_id",
            nullable = false,
            updatable = false,
            referencedColumnName = "id")
    private Author author;
}
