package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleLearnedCount {
    private int value;
    private final static int MIN_VALUE = 0;
    private final static int MAX_VALUE = 999_999_999;

    private ArticleLearnedCount(int value) {
        this.value = value;
    }

    public static ArticleLearnedCount of(int value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        return new ArticleLearnedCount(value);
    }
}

