package jp.boosty.graphql.domain.domainmodel.search;

import jp.boosty.graphql.infrastructure.constant.edge.EdgeLabel;

import io.netty.util.internal.StringUtil;

public class SearchFilter {
    private String value;
    private final static String DRAFTED = EdgeLabel.DRAFT.getString();
    private final static String PUBLISHED = EdgeLabel.PUBLISH.getString();
    private final static String SUSPENDED = EdgeLabel.SUSPEND.getString();
    private final static String ALL = "all";

    private SearchFilter(String value) {
        this.value = value;
    }

    public static SearchFilter of(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            return new SearchFilter(ALL);
        }

        if (value.equals(DRAFTED) || value.equals(PUBLISHED) || value.equals(ALL)) {
            return new SearchFilter(value);
        }

        return new SearchFilter(ALL);
    }

    public String getValue() {
        return value;
    }

    public boolean shouldFilter() {
        return !value.equals(ALL);
    }

    public boolean createdFilter() {
        return value.equals(DRAFTED) || value.equals(PUBLISHED) || value.equals(SUSPENDED) || value.equals(ALL);
    }
}
