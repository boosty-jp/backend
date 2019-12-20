package co.jp.wever.graphql.domain.domainmodel.search;

import co.jp.wever.graphql.infrastructure.constant.vertex.property.DateProperty;

public class SearchSortField {

    private String value;
    private final static String NONE = "none";

    private SearchSortField(String value) {
        this.value = value;
    }

    public static SearchSortField of(String value) {
        if (DateProperty.isDefined(value) || UserToContentProperty.isDefined(value)) {
            return new SearchSortField(value);
        }

        return new SearchSortField(NONE);
    }

    public boolean hasField() {
        return !value.equals(NONE);
    }

    public boolean edgeSort() {
        return UserToContentProperty.isDefined(value);
    }

    public boolean vertexSort() {
        return DateProperty.isDefined(value);
    }

    public String getValue() {
        return value;
    }
}
