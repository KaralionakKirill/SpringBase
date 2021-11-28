package ru.karalionak.rest.service;

import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.query.CertificateColumns;
import ru.karalionak.rest.query.Direction;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CertificateService {

    void create(Certificate certificate);

    void update(Certificate certificate);

    void delete(long id);

    Optional<Certificate> findById(long id);

    List<Certificate> getAllWithTags(String tagName, String regex, Map<CertificateColumns, Direction> context);
}
