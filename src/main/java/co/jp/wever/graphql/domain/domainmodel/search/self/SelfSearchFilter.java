package co.jp.wever.graphql.domain.domainmodel.search.self;

import co.jp.wever.graphql.infrastructure.constant.edge.label.UserToContentProperty;

public class SelfSearchFilter {
    private String value;
    private final static String DRAFTED = UserToContentProperty.DRAFTED.getString();
    private final static String PUBLISHED = UserToContentProperty.PUBLISHED.getString();
    private final static String ALL = "all";

    private SelfSearchFilter(String value) {
        this.value = value;
    }

    public static SelfSearchFilter of(String value) {
        if (value.equals(DRAFTED) || value.equals(PUBLISHED) || value.equals(ALL)) {
            return new SelfSearchFilter(value);
        }

        return new SelfSearchFilter(ALL);
    }

    public String getValue() {
        return value;
    }

    public boolean shouldFilter() {
        return value.equals(ALL);
    }
}
