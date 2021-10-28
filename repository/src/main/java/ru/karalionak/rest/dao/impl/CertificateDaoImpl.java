package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.karalionak.rest.dao.CertificateDao;
import ru.karalionak.rest.entity.Certificate;
import ru.karalionak.rest.query.OrderedViewQuery;
import ru.karalionak.rest.query.impl.QueryHelperImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateDaoImpl extends AbstractDao<Certificate> implements CertificateDao {
    private static final String CREATE_CERTIFICATE =
            "INSERT INTO gift_certificates(certificate_name, description, price, duration) VALUES(?, ?, ?, ?)";
    private static final String CREATE_REFERENCE_BETWEEN_CERTIFICATE_TAG =
            "INSERT INTO gift_certificate_tag(certificate_id, tag_id) VALUES(?,?)";
    private static final String FIND_CERTIFICATE_BY_TAG_ID =
            "SELECT * FROM gift_certificates JOIN gift_certificate_tag gct on gift_certificates.id = gct.certificate_id " +
                    "WHERE gct.tag_id=?";
    private final static String TABLE_NAME = "gift_certificates";
    private final QueryHelperImpl queryHelper;
    private final ResultSetExtractor<List<Certificate>> certificateExtractor;

    public CertificateDaoImpl(JdbcTemplate jdbcTemplate, QueryHelperImpl queryHelper,
                              RowMapper<Certificate> certificateRowMapper,
                              ResultSetExtractor<List<Certificate>> certificateExtractor) {
        super(jdbcTemplate, certificateRowMapper, TABLE_NAME);
        this.queryHelper = queryHelper;
        this.certificateExtractor = certificateExtractor;
    }

    @Override
    public void create(Certificate certificate) {
        jdbcTemplate.update(CREATE_CERTIFICATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration());
    }

    @Override
    public void update(Certificate certificate) {
        Map<String, Object> updatedFields = findUpdatedFields(certificate);
        if(updatedFields.size() > 0){
            String query = updateQuery + queryHelper.buildUpdateQuery(updatedFields.keySet());
            jdbcTemplate.update(query, updatedFields.values().toArray());
        }
    }

    @Override
    public void addTags(long certificateId, List<Long> tagIds) {
        jdbcTemplate.batchUpdate(CREATE_REFERENCE_BETWEEN_CERTIFICATE_TAG, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, certificateId);
                ps.setLong(2, tagIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return tagIds.size();
            }
        });
    }


    @Override
    public List<Certificate> findCertificateByTagId(long tagId) {
        return jdbcTemplate.query(FIND_CERTIFICATE_BY_TAG_ID, rowMapper, tagId);
    }

    @Override
    public List<Certificate> findCertificateWithTags(OrderedViewQuery orderedViewQuery) {
        return jdbcTemplate.query(orderedViewQuery.getQuery(), certificateExtractor, orderedViewQuery.getValues());
    }


    @Override
    public Optional<Certificate> findByName(String name) {
        return findByColumn("certificate_name", name).stream().findAny();
    }

    private Map<String, Object> findUpdatedFields(Certificate  certificate){
        Map<String, Object> fields = new LinkedHashMap<>();
        if(certificate.getName() != null){
            fields.put("certificate_name", certificate.getName());
        }
        if(certificate.getDescription() != null){
            fields.put("description", certificate.getDescription());
        }
        if(certificate.getPrice() != null){
            fields.put("price", certificate.getPrice());
        }
        if(certificate.getDuration() > 0){
            fields.put("duration", certificate.getDuration());
        }
        if(certificate.getCreateDate() != null){
            fields.put("create_date", certificate.getCreateDate());
        }
        return fields;
    }
}
