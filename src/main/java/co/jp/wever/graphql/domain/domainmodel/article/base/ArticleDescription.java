package co.jp.wever.graphql.domain.domainmodel.article.base;

public class ArticleDescription {

    private String value;
    private final static int MAX_WORD_COUNT = 1000;

    private ArticleDescription(String value) {
        this.value = value;
    }

    public static ArticleDescription of(String value) throws IllegalArgumentException {
        if (value.length() > MAX_WORD_COUNT) {
            throw new IllegalArgumentException();
        }

        return new ArticleDescription(value);
    }

    public String getValue() {
        return value;
    }
}

