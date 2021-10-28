package ru.karalionak.rest.query;

public enum CertificateColumns {
    ID("id"),
    CERTIFICATE_NAME("certificate_name"),
    DESCRIPTION("description"),
    PRICE("price"),
    CREATE_DATE("create_date"),
    LAST_UPDATE_DATE("last_update_date"),
    DURATION("duration");

    CertificateColumns(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
