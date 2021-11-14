package ru.karalionak.rest.query;

import java.util.Set;

public interface QueryHelper {
    String buildUpdateQuery(Set<String> columns);

    String buildSortingQuery(SortingContext sortingContext);

    String buildRegex(String pattern);
}
