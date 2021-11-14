package ru.karalionak.rest.dao;

import ru.karalionak.rest.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends Dao<Tag>{

    Optional<Tag> findByName(String name);

    List<Tag> findByCertificateId(long certificateId);
}
