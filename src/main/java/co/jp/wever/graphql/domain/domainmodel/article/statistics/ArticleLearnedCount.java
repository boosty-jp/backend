package co.jp.wever.graphql.domain.domainmodel.article.statistics;

public class ArticleLearnedCount {

    private long value;
    private final static long MIN_VALUE = 0;
    private final static long MAX_VALUE = 999_999_999;

    private ArticleLearnedCount(long value) {
        this.value = value;
    }

    public static ArticleLearnedCount of(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException();
        }

        if (value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }

        return new ArticleLearnedCount(value);
    }

    public long getValue() {
        return value;
    }
}

