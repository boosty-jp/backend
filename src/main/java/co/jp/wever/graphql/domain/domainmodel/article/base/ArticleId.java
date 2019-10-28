package co.jp.wever.graphql.domain.domainmodel.article.base;

public class ArticleId {

    private String value;

    private ArticleId(String value) {
        this.value = value;
    }

    // TODO: ドメインじゃないので消したい
    public static ArticleId of(String value) {

        return new ArticleId(value);
    }

    public String getValue() {
        return value;
    }

}
