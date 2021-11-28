package ru.karalionak.rest.facade;

import ru.karalionak.rest.dto.CertificateDto;
import ru.karalionak.rest.query.CertificateColumns;
import ru.karalionak.rest.query.Direction;

import java.util.List;

public interface CertificateFacade {
    void create(CertificateDto certificateDto);

    void update(long id, CertificateDto certificateDto);

    void delete(long id);

    CertificateDto findById(long id);

    List<CertificateDto> getAllWithTags(String tagName, String regex, List<CertificateColumns> columns, List<Direction> directions);
}
