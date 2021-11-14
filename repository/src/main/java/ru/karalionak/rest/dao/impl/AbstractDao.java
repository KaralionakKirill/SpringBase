package ru.karalionak.rest.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.karalionak.rest.dao.Dao;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> implements Dao<T> {
    public final String findByIdQuery;
    public final String findByColumnQuery;
    public final String deleteByIdQuery;
    public final String getAllQuery;
    public final String updateQuery;

    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    public AbstractDao(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper, String tableName) {
        findByIdQuery = "SELECT * FROM " + tableName + " WHERE id=?";
        findByColumnQuery = "SELECT * FROM " + tableName + " WHERE %s=?";
        deleteByIdQuery = "DELETE FROM " + tableName + " WHERE id=?";
        getAllQuery = "SELECT * FROM " + tableName;
        updateQuery = "UPDATE " + tableName + " SET ";
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    public void delete(long id){
        jdbcTemplate.update(deleteByIdQuery, id);
    }

    public Optional<T> findById(long id){
        return jdbcTemplate.query(findByIdQuery, rowMapper, id).stream().findAny();
    }

    protected List<T> findByColumn(String column, String value){
        String query = String.format(findByColumnQuery, column);
        return jdbcTemplate.query(query, rowMapper, value);
    }
}
