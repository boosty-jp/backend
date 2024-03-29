package jp.boosty.backend.domain.domainmodel.search;

import jp.boosty.backend.infrastructure.constant.vertex.property.DateProperty;

import io.netty.util.internal.StringUtil;

public class SearchSortField {

    private String value;
    private final static String NONE = "none";
    private final static String CREATE_TIME = DateProperty.CREATE_TIME.getString();
    private final static String UPDATE_TIME = DateProperty.UPDATE_TIME.getString();

    private SearchSortField(String value) {
        this.value = value;
    }

    public static SearchSortField of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new SearchSortField(NONE);
        }

        if (value.equals(CREATE_TIME) || value.equals(UPDATE_TIME)) {

            return new SearchSortField(NONE);
        }

        return new SearchSortField(NONE);
    }

    public boolean hasField() {
        return !value.equals(NONE);
    }

    public boolean vertexSort() {
        return value.equals(CREATE_TIME) || value.equals(UPDATE_TIME);
    }

    public String getValue() {
        return value;
    }
}
