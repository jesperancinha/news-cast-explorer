package org.jesperancinha.twitter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class Message {

    private String id;

    @EqualsAndHashCode.Exclude
    private Date createdAt;

    @EqualsAndHashCode.Exclude
    private String text;

    @EqualsAndHashCode.Exclude
    private User user;
}
