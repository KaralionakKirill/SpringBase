package ru.karalionak.rest.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.karalionak.rest.entity.Certificate;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CertificateMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return map(resultSet);
    }

    protected Certificate map(ResultSet resultSet) throws SQLException {
        return new Certificate(
                resultSet.getLong("id"),
                resultSet.getString("certificate_name"),
                resultSet.getString("description"),
                resultSet.getBigDecimal("price"),
                resultSet.getInt("duration"),
                resultSet.getTimestamp("create_date").toLocalDateTime(),
                resultSet.getTimestamp("last_update_date").toLocalDateTime()
        );
    }
}
