package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.karalionak.rest.dao.TagDao;
import ru.karalionak.rest.entity.Tag;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    private static final String CREATE_TAG = "INSERT INTO tags(tag_name) VALUES(?)";
    private static final String FIND_TAGS_BY_CERTIFICATE_ID = "SELECT * FROM tags " +
            "JOIN gift_certificate_tag gct on tags.id = gct.tag_id WHERE gct.certificate_id=?";
    private static final String TABLE_NAME = "tags";

    public TagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> tagRowMapper) {
        super(jdbcTemplate, tagRowMapper, TABLE_NAME);
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(CREATE_TAG, tag.getName());
    }

    @Override
    public void update(Tag entity) {
        throw new UnsupportedOperationException("Method \"update\" is unsupported");
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return findByColumn("tag_name", name).stream().findAny();
    }

    @Override
    public List<Tag> findByCertificateId(long certificateId) {
        return jdbcTemplate.query(FIND_TAGS_BY_CERTIFICATE_ID, rowMapper, certificateId);
    }
}
