package ru.karalionak.rest.dao;

import ru.karalionak.rest.entity.Tag;

import java.util.List;
import java.util.Set;

public interface TagDao extends Dao<Tag>{

    List<Tag> findExistingTags(Set<Tag> tags);

    List<Long> createTags(List<String> names);

    List<Tag> findByCertificateId(long id);
}
