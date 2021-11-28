package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.karalionak.rest.dao.TagDao;
import ru.karalionak.rest.entity.Tag;
import ru.karalionak.rest.query.QueryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl extends AbstractDao<Tag> implements TagDao {
    public static final String FIND_BY_CERTIFICATE_ID = "SELECT * FROM tags " +
            "JOIN gift_certificate_tag gct on tags.id = gct.tag_id WHERE gct.certificate_id=?";
    public static final String CREATE_TAGS = "INSERT INTO tags(tag_name)";
    private static final String CREATE_TAG = "INSERT INTO tags(tag_name) VALUES(:name) RETURNING id";
    private static final String TABLE_NAME = "tags";

    public TagDaoImpl(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate,
                      RowMapper<Tag> tagRowMapper, QueryHelper queryHelper) {
        super(namedJdbcTemplate, jdbcTemplate, tagRowMapper, TABLE_NAME, queryHelper);
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(tag);
        namedJdbcTemplate.update(CREATE_TAG, parameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey(),
                "Database did not return primary key after tag creation").longValue();
    }

    public List<Long> createTags(List<String> names) {
        String query = CREATE_TAGS + queryHelper.buildInsertingQuery(names.size()) + "RETURNING id";
        List<Long> generatedIds = new ArrayList<>();
        jdbcTemplate.query(query, (rs) -> {
            generatedIds.add(rs.getLong("id"));
        }, names);
        return generatedIds;
    }

    @Override
    public List<Tag> findByCertificateId(long id) {
        return jdbcTemplate.query(FIND_BY_CERTIFICATE_ID, rowMapper, id);
    }

    @Override
    public List<Tag> findExistingTags(Set<Tag> tags) {
        String query = findAllQuery + queryHelper.buildFilteringQuery("tag_name", tags.size());
        List<String> tagNames = tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList());
        return jdbcTemplate.query(query, rowMapper, tagNames);
    }

    @Override
    public void update(Tag entity) {
        throw new UnsupportedOperationException("Method \"update\" is unsupported");
    }

}
