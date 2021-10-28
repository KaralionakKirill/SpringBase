package ru.karalionak.rest.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.entity.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class CertificateExtractor implements ResultSetExtractor<List<Certificate>> {
    private final static CertificateMapper CERTIFICATE_MAPPER = new CertificateMapper();
    private final static TagMapper TAG_MAPPER = new TagMapper();

    @Override
    public List<Certificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Long, Set<Tag>> certificatesTags = new LinkedHashMap<>();
        List<Certificate> certificateList = new ArrayList<>();
        while (resultSet.next()) {
            Certificate certificate = CERTIFICATE_MAPPER.map(resultSet);
            long certificateId = certificate.getId();
            certificatesTags.putIfAbsent(certificateId, new HashSet<>());
            Tag tag = TAG_MAPPER.map(resultSet);
            certificatesTags.get(certificateId).add(tag);
            certificateList.add(certificate);
        }
        tagDistribution(certificateList, certificatesTags);
        return certificateList;
    }

    private void tagDistribution(List<Certificate> certificateList,  Map<Long, Set<Tag>> certificatesTags){
        certificateList.forEach(certificate -> {
            long certificateId = certificate.getId();
            certificate.setTags(certificatesTags.get(certificateId));
        });
    }
}
