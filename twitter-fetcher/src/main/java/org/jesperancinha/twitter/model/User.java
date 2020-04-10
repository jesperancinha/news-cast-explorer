package org.jesperancinha.twitter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class User {

    private String id;

    @EqualsAndHashCode.Exclude
    private Date createdAt;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    private String screenName;
}
