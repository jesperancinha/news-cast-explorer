package org.jesperancinha.twitter.service;

import org.jesperancinha.twitter.data.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getMessages();
}
