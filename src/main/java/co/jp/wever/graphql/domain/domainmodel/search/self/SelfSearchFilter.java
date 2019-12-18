package co.jp.wever.graphql.domain.domainmodel.search.self;

public class SelfSearchFilter {
    private String value;
    private final static String DRAFTED = "drafted";
    private final static String PUBLISHED = "published";
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

}
