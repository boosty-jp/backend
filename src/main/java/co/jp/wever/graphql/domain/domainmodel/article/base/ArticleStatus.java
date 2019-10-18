package co.jp.wever.graphql.domain.domainmodel.article.base;

public enum ArticleStatus {
    PUBLISHED("published"), DRAFTED("drafted"), DELETED("deleted");

    private String value;

    private ArticleStatus(String value) {
        this.value = value;
    }
}
