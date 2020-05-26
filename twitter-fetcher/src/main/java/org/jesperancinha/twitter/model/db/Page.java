package org.jesperancinha.twitter.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private Long duration;

    private Long createdAt;

    @OneToMany(mappedBy = "page", cascade = CascadeType.MERGE)
    private final List<Author> authors = new ArrayList<>();
}
