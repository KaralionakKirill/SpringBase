package ru.karalionak.rest.service;

import ru.karalionak.rest.entity.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagService {
    void create(Tag tag);

    void delete(long id);

    Optional<Tag> findById(long id);

    List<Tag> findExistingTags(Set<Tag> tags);

    List<Long> createTags(List<String> names);

    List<Tag> findByCertificateId(long id);
}
