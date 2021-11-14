package ru.karalionak.rest.query;

import java.util.Map;

public class SortingContext {
    private final Map<CertificateColumns, Direction> context;

    public SortingContext(Map<CertificateColumns, Direction> context) {
        this.context = context;
    }

    public Map<CertificateColumns, Direction> getContext() {
        return context;
    }
}
