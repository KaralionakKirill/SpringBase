package ru.karalionak.rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.karalionak.rest.entity.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return map(resultSet);
    }

    protected Tag map(ResultSet resultSet) throws SQLException {
        return new Tag(
                resultSet.getLong("id"),
                resultSet.getString("tag_name")
        );
    }
}
