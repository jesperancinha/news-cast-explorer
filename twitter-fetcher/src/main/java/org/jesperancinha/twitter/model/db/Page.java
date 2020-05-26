package org.jesperancinha.twitter.model.db;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Page {

    @Id
    private Long id;

    private Long duration;

    private Long createdAt;

    @OneToMany(mappedBy = "page")
    private List<Author> authors;
}
