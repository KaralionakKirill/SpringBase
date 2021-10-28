package ru.karalionak.rest.query.impl;

import org.springframework.stereotype.Component;
import ru.karalionak.rest.query.CertificateColumns;
import ru.karalionak.rest.query.Direction;
import ru.karalionak.rest.query.QueryHelper;
import ru.karalionak.rest.query.SortingContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QueryHelperImpl implements QueryHelper {
    public static final String ANY_STRING_REGEX = "%";

    @Override
    public String buildUpdateQuery(Set<String> columns) {
        StringBuilder query = new StringBuilder("last_update_date=NOW()");
        for (String column : columns) {
            query.append(", ")
                    .append(column)
                    .append("=?");
        }
        query.append(" WHERE id=?");
        return query.toString();
    }

    @Override
    public String buildSortingQuery(SortingContext sortingContext) {
        StringBuilder query = new StringBuilder(" ORDER BY ");
        Map<CertificateColumns, Direction> context = sortingContext.getContext();
        String conditions = context.entrySet()
                .stream()
                .map((entry) -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining(", "));
        query.append(conditions);
        return query.toString();
    }


    @Override
    public String buildRegex(String pattern) {
        return String.format("%s%s%s", ANY_STRING_REGEX, pattern, ANY_STRING_REGEX);
    }
}
