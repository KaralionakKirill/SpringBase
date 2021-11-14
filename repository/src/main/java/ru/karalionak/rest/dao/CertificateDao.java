package ru.karalionak.rest.dao;


import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.query.OrderedViewQuery;

import java.util.List;
import java.util.Optional;

public interface CertificateDao extends Dao<Certificate> {
    
    void addTags(long certificateId, List<Long> tagIds);

    List<Certificate> findCertificateByTagId(long tagId);

    List<Certificate> findCertificateWithTags(OrderedViewQuery orderedViewQuery);

    Optional<Certificate> findByName(String name);
}
