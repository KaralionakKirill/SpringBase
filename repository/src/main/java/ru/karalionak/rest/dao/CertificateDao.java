package ru.karalionak.rest.dao;


import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.query.OrderedViewQuery;

import java.util.List;

public interface CertificateDao extends Dao<Certificate> {
    
    void addTags(long certificateId, List<Long> tagIds);

    List<Certificate> findCertificateWithTags(OrderedViewQuery orderedViewQuery);
}
