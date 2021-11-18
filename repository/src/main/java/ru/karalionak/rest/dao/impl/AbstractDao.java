package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.karalionak.rest.dao.Dao;
import ru.karalionak.rest.query.QueryHelper;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> implements Dao<T> {
    public final String findByIdQuery;
    public final String findByColumnQuery;
    public final String deleteByIdQuery;
    public final String findAllQuery;
    public final String updateQuery;

    protected final NamedParameterJdbcTemplate namedJdbcTemplate;
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;
    protected final QueryHelper queryHelper;

    public AbstractDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper,
                       String tableName, QueryHelper queryHelper) {
        findByIdQuery = "SELECT * FROM " + tableName + " WHERE id=?";
        findByColumnQuery = "SELECT * FROM " + tableName + " WHERE %s=?";
        deleteByIdQuery = "DELETE FROM " + tableName + " WHERE id=?";
        findAllQuery = "SELECT * FROM " + tableName;
        updateQuery = "UPDATE " + tableName + " SET ";
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
        this.queryHelper = queryHelper;
    }

    public void delete(long id) {
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    public Optional<T> findById(long id) {
        return jdbcTemplate.query(findByIdQuery, rowMapper, id).stream().findAny();
    }

    protected List<T> findByColumn(String column, String value) {
        String query = String.format(findByColumnQuery, column);
        return jdbcTemplate.query(query, rowMapper, value);
    }
}
