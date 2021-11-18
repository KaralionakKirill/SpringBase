package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.karalionak.rest.dao.CertificateDao;
import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.query.OrderedViewQuery;
import ru.karalionak.rest.query.QueryHelper;

import java.util.*;

@Repository
public class CertificateDaoImpl extends AbstractDao<Certificate> implements CertificateDao {
    public static final String FIND_CERTIFICATE_WITH_TAGS = "SELECT * FROM gift_certificates " +
            "JOIN gift_certificate_tag gct on gift_certificates.id = gct.certificate_id " +
            "JOIN tags on tags.id = gct.tag_id WHERE id=?";
    private static final String CREATE_CERTIFICATE =
            "INSERT INTO gift_certificates(certificate_name, description, price, duration) " +
                    "VALUES(:name, :description, :price, :duration) RETURNING id";
    private static final String CREATE_REFERENCE_BETWEEN_CERTIFICATE_TAG =
            "INSERT INTO gift_certificate_tag(certificate_id, tag_id) VALUES(?,?)";
    private final static String TABLE_NAME = "gift_certificates";
    private final ResultSetExtractor<List<Certificate>> certificateExtractor;

    public CertificateDaoImpl(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate,
                              RowMapper<Certificate> certificateRowMapper, QueryHelper queryHelper,
                              ResultSetExtractor<List<Certificate>> certificateExtractor) {
        super(namedJdbcTemplate, jdbcTemplate, certificateRowMapper, TABLE_NAME, queryHelper);
        this.certificateExtractor = certificateExtractor;
    }

    @Override
    public long create(Certificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(certificate);
        namedJdbcTemplate.update(CREATE_CERTIFICATE, parameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(),
                "Database did not return primary key after certificate creation").longValue();
    }

    @Override
    public void update(Certificate certificate) {
        Map<String, Object> updatedFields = findUpdatedFields(certificate);
        if (updatedFields.size() > 0) {
            String query = updateQuery + queryHelper.buildUpdateQuery(updatedFields.keySet());
            Collection<Object> values = updatedFields.values();
            values.add(certificate.getId());
            jdbcTemplate.update(query, values.toArray());
        }
    }

    @Override
    public void addTags(long certificateId, List<Long> tagIds) {
        BeanPropertySqlParameterSource[] parameters = tagIds.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        namedJdbcTemplate.batchUpdate(CREATE_REFERENCE_BETWEEN_CERTIFICATE_TAG, parameters);
    }


    @Override
    public List<Certificate> findCertificateWithTags(OrderedViewQuery orderedViewQuery) {
        return jdbcTemplate.query(orderedViewQuery.getQuery(), certificateExtractor, orderedViewQuery.getValues());
    }


    private Map<String, Object> findUpdatedFields(Certificate certificate) {
        Map<String, Object> fields = new LinkedHashMap<>();
        if (certificate.getName() != null) {
            fields.put("certificate_name", certificate.getName());
        }
        if (certificate.getDescription() != null) {
            fields.put("description", certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            fields.put("price", certificate.getPrice());
        }
        if (certificate.getDuration() > 0) {
            fields.put("duration", certificate.getDuration());
        }
        if (certificate.getCreateDate() != null) {
            fields.put("create_date", certificate.getCreateDate());
        }
        return fields;
    }
}
