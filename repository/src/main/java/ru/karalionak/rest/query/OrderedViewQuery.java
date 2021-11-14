package ru.karalionak.rest.query;

import ru.karalionak.rest.query.impl.QueryHelperImpl;

import java.util.ArrayList;
import java.util.List;

public class OrderedViewQuery {
    private static final QueryHelperImpl queryHelper = new QueryHelperImpl();
    private static final String QUERY_BEGINNING = "SELECT * FROM gift_certificates " +
            "JOIN gift_certificate_tag gct on gift_certificates.id = gct.certificate_id " +
            "JOIN tags on tags.id = gct.tag_id";
    private final StringBuilder query;
    private final List<Object> values;

    public OrderedViewQuery(StringBuilder query, List<Object> values) {
        this.query = query;
        this.values = values;
    }

    public String getQuery() {
        return query.toString();
    }

    public List<Object> getValues() {
        return values;
    }

    public static QueryBuilder builder() {
        return new QueryBuilder();
    }

    public static class QueryBuilder {
        private String tagName;
        private String regex;
        private SortingContext sortingContext;

        public QueryBuilder filteringByTagName(String tagName) {
            this.tagName = tagName;
            return this;
        }

        public QueryBuilder filteringByNameOrDescription(String regex) {
            this.regex = regex;
            return this;
        }

        public QueryBuilder sortingQuery(SortingContext sortingContext) {
            this.sortingContext = sortingContext;
            return this;
        }

        public OrderedViewQuery build() {
            StringBuilder query = new StringBuilder(QUERY_BEGINNING);
            List<Object> values = new ArrayList<>();
            if (tagName != null || regex != null) {
                query.append(" WHERE ");
            }
            if (tagName != null) {
                query.append("(tags.tag_name=?)");
                values.add(tagName);
            }
            if (regex != null) {
                if (tagName != null) {
                    query.append(" OR ");
                }
                query.append("(certificate_name LIKE ? OR description LIKE ?)");
                values.add(queryHelper.buildRegex(regex));
                values.add(queryHelper.buildRegex(regex));
            }
            if (sortingContext != null) {
                query.append(queryHelper.buildSortingQuery(sortingContext));
            }
            return new OrderedViewQuery(query, values);
        }
    }
}
