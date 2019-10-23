package co.jp.wever.graphql.domain.domainmodel.article.base;

public enum ArticleStatus {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted");

    private String value;

    ArticleStatus(String value) {
        this.value = value;
    }

    public String getString() {
        return this.value;
    }

    public static ArticleStatus fromString(String value) {
        for (ArticleStatus s : ArticleStatus.values()) {
            if (s.value.equalsIgnoreCase(value)) {
                return s;
            }
        }
        return null;
    }
}
